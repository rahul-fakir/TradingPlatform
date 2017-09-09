package com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels;

import io.realm.RealmObject;

/**
 * Created by rahulfakir on 7/23/17.
 */

public class RealmWalletModel extends RealmObject {
    private String walletId, currencyCode, name;
    private Double balance, currentPrice, roundUpBalance;

    public RealmWalletModel() {
        this.currentPrice = -1.0;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getRoundUpBalance() {
        return roundUpBalance;
    }

    public void setRoundUpBalance(Double roundUpBalance) {
        this.roundUpBalance = roundUpBalance;
    }
}
