package aclass.android.adam.project4;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aclass.android.adam.project4.data.CategoryDao;
import aclass.android.adam.project4.data.DenominationDao;
import aclass.android.adam.project4.data.LookupTableDao;
import aclass.android.adam.project4.data.OpenShiftSyncDao;
import aclass.android.adam.project4.data.PurchaseDao;
import aclass.android.adam.project4.data.ShopDao;


/**
 * Android RestTask (REST) from the Android Recipes book.
 */
@TargetApi(16)
public class OpenShiftSyncRestTask extends AsyncTask<Void, Void, List<String>>
{

    public static final String HTTP_RAILS_CATEGORIES_JSON = "http://rails-pgsql-persistent-my-node-mongo.7e14.starter-us-west-2.openshiftapps.com/categories.json";
    public static final String HTTP_RAILS_DENOMINATIONS_JSON = "http://rails-pgsql-persistent-my-node-mongo.7e14.starter-us-west-2.openshiftapps.com/denominations.json";
    public static final String HTTP_RAILS_SHOPS_JSON = "http://rails-pgsql-persistent-my-node-mongo.7e14.starter-us-west-2.openshiftapps.com/shops.json";
    Context context;
    ProgressDialog progressDialog;

    private PurchaseDao purchaseDao;
    private CategoryDao categoryDao;
    private DenominationDao denominationDao;
    private ShopDao shopDao;
    private OpenShiftSyncDao openShiftSyncDao;
    private TextView mOutputText;


    public OpenShiftSyncRestTask(SheetsSyncActivity context) {
        this.context = context;
        purchaseDao = new PurchaseDao(context);
        categoryDao = new CategoryDao(context);
        denominationDao = new DenominationDao(context);
        shopDao = new ShopDao(context);
        openShiftSyncDao = new OpenShiftSyncDao(context);

        mOutputText = new TextView(context);
        mOutputText.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText("");
        context.activityLayout.addView(mOutputText);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "downloading", "please wait");
    }

    @Override
    protected void onPostExecute(List<String> output) {
        Log.i("OPENSHIFT", "RESULT = " + output);
        progressDialog.dismiss();
        if (output == null || output.size() == 0) {
            mOutputText.setText("No results returned.");
        } else {
            output.add(0, "Data retrieved using OpenShift Rails API:");
            mOutputText.setText(TextUtils.join("\n", output));
        }
    }

    @Override
    protected void onCancelled() {
        progressDialog.dismiss();
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        openShiftSyncDao.ensureTable();
        List<String> results = new ArrayList<>();

        openShiftSyncDao.loadSavedSheetIds();
        JSONArray jsonArray = getPurchases("http://rails-pgsql-persistent-my-node-mongo.7e14.starter-us-west-2.openshiftapps.com/purchases.json");
        String errorPrefix = "unknown Openshiftresttask error";
        int foundCount =0;
        int addedCount = 0;
        try {
            for (int i=0; i < jsonArray.length(); i++) {
                foundCount++;
                long id = jsonArray.getJSONObject(i).getLong("id");
                if (!openShiftSyncDao.isIdSaved(id)) {
                    String commonName = jsonArray.getJSONObject(i).getString("common_name");
                    errorPrefix = id + " " + commonName + " error: ";
                    String specificName = jsonArray.getJSONObject(i).getString("specific_name");
                    long categoryId = jsonArray.getJSONObject(i).getLong("category_id");
                    long shopId = jsonArray.getJSONObject(i).getLong("shop_id");
                    double price = jsonArray.getJSONObject(i).getDouble("price");
                    double quantity = jsonArray.getJSONObject(i).getDouble("quantity");
                    Double subquantity = Util.doubleOrNull(jsonArray.getJSONObject(i).getString("subquantity"), results, errorPrefix);
                    long denominationId = jsonArray.getJSONObject(i).getLong("denomination_id");
                    boolean isSale = jsonArray.getJSONObject(i).getBoolean("is_sale");
                    long createdAt = Util.getTimeMillisForTimestamp( jsonArray.getJSONObject(i).getString("created_at") );
                    long localCategoryId = translateId(categoryId, HTTP_RAILS_CATEGORIES_JSON, categoryDao);
                    long localDenominationId = translateId(denominationId, HTTP_RAILS_DENOMINATIONS_JSON, denominationDao);
                    long localShopId = translateId(shopId, HTTP_RAILS_SHOPS_JSON, shopDao);
                    try {
                        long newId = purchaseDao.insertPurchase(commonName, specificName, localCategoryId,
                                localShopId, price, quantity, subquantity, localDenominationId, isSale,
                                createdAt);
                        if (newId > 0) {
                            openShiftSyncDao.insert(newId, id);
                            addedCount++;
                        } else {
                            results.add(errorPrefix + " failed to insertLookup");
                        }
                    } catch (Exception e) {
                        results.add(errorPrefix + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            results.add(errorPrefix + e.getMessage());
            e.printStackTrace();
        }
        results.add("Found count: " + foundCount + ". Added count: " + addedCount);
        return results;
    }

    private long translateId(long lookupTableId, String osUrl, LookupTableDao lookupTableDao) {
        Map<Long, String> lookupTableMap = getLookupsMap(osUrl);
        String remoteName = lookupTableMap.get(lookupTableId);
        Long newId = lookupTableDao.getId(remoteName);
        if (newId == null) {
            newId = lookupTableDao.insertLookup(lookupTableId, remoteName);
        }
        return newId;
    }

    private JSONArray getPurchases(String urlStr) {
        JSONArray jsonArray = null;
        String jsonResult = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            jsonResult = inputStreamToString(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(jsonResult);
//            for (int i=0; i < jsonArray.length(); i++) {
//                String id = jsonArray.getJSONObject(i).getString("id");
//                String name = jsonArray.getJSONObject(i).getString("name");
//                longStringMap.put(Long.parseLong(id), name);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    private static Map<String, Map<Long, String>> lookupUrlCache = new HashMap<>();

    private Map<Long, String> getLookupsMap(String urlStr) {
        if (lookupUrlCache.containsKey(urlStr)) {
            return lookupUrlCache.get(urlStr);
        }
        Map<Long, String> longStringMap = new HashMap<>();
        String jsonResult = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            jsonResult = inputStreamToString(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            for (int i=0; i < jsonArray.length(); i++) {
                String id = jsonArray.getJSONObject(i).getString("id");
                String name = jsonArray.getJSONObject(i).getString("name");
                longStringMap.put(Long.parseLong(id), name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lookupUrlCache.put(urlStr, longStringMap);
        return longStringMap;
    }

    private String inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }
}

