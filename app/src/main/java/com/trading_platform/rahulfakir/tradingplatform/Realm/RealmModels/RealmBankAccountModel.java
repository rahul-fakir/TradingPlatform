package com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels;

import io.realm.RealmObject;

/**
 * Created by rahulfakir on 7/31/17.
 */

public class RealmBankAccountModel extends RealmObject {
    private String accountId;

    public RealmBankAccountModel(){

    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
