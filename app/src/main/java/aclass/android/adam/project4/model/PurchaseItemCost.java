package aclass.android.adam.project4.model;

import java.text.DecimalFormat;

/**
 * Created by adam on 11/5/2017.
 */

public class PurchaseItemCost {
    private String commonName;
    private String specificName;
    private String denomination;
    private String shopName;
    private double highPriceRatio;
    private double lowPriceRatio;

    public PurchaseItemCost(String commonName, String specificName, double lowPriceRatio, double highPriceRatio,
                            String denomination, String shopName) {
        this.commonName = commonName;
        this.specificName = specificName;
        this.lowPriceRatio = lowPriceRatio;
        this.highPriceRatio = highPriceRatio;
        this.denomination = denomination;
        this.shopName = shopName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(String specificName) {
        this.specificName = specificName;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public double getHighPriceRatio() {
        return highPriceRatio;
    }

    public void setHighPriceRatio(double highPriceRatio) {
        this.highPriceRatio = highPriceRatio;
    }

    public double getLowPriceRatio() {
        return lowPriceRatio;
    }

    public void setLowPriceRatio(double lowPriceRatio) {
        this.lowPriceRatio = lowPriceRatio;
    }

    @Override
    public String toString() {
        DecimalFormat formatter = new java.text.DecimalFormat("0.00##");
        return (commonName + " ").trim() +
                ((commonName.length() > 0 && specificName.length() > 0) ? " / " : "") +
                specificName + " ($" + formatter.format(lowPriceRatio) +
                (Double.compare(lowPriceRatio, highPriceRatio) != 0 ?
                        "-" + formatter.format(highPriceRatio) : "") +
        "/" + denomination + ") " + shopName.trim();
    }
}
