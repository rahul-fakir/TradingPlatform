package com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels;

import io.realm.RealmObject;

/**
 * Created by rahulfakir on 7/29/17.
 */

public class RealmHistoricalPriceModel extends RealmObject {

    //  Private Variables
    private Float price;
    private Float timeStamp;
    private String historicalIndex, currencyCode;

    public RealmHistoricalPriceModel() {
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Float timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHistoricalIndex() {
        return historicalIndex;
    }

    public void setHistoricalIndex(String historicalIndex) {
        this.historicalIndex = historicalIndex;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
