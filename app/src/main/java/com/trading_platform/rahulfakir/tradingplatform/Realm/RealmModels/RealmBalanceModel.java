package com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels;

import io.realm.RealmObject;

/**
 * Created by rahulfakir on 7/26/17.
 */

public class RealmBalanceModel extends RealmObject {
    private Double currentBalance;

    public RealmBalanceModel() {
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
