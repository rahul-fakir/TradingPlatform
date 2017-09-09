package com.trading_platform.rahulfakir.tradingplatform.Firebase.ObjectHandlers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmBalanceModel;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmHistoricalPriceModel;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmWalletModel;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by rahulfakir on 7/25/17.
 */

public class FirebaseHistoricalPriceHandler {

    //  Firebase
    private FirebaseDatabase database;
    private String baseCurrencyCode;

    //  Realm
    private Realm realm;

    //  Internal Variables
    private String[] historicalPriceIndexes = {"pastHour", "pastDay"};


    public FirebaseHistoricalPriceHandler() {
        realm = Realm.getDefaultInstance();
        database = FirebaseDatabase.getInstance();

        baseCurrencyCode = "USD";

        initializeAndListenCurrentData();

        for (int i = 0; i < historicalPriceIndexes.length; i++) {
            initializeAndListenHistorical(historicalPriceIndexes[i]);
        }

    }

    private void initializeAndListenCurrentData() {
        DatabaseReference currentPricingRef = database.getReference("historicalPricing/" + baseCurrencyCode  + "/current");
        currentPricingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // System.out.println(dataSnapshot.getKey() + " " + dataSnapshot.getValue());
                updateCurrentPrice(dataSnapshot.getKey(), Double.valueOf(String.format("%.2f", Double.valueOf(dataSnapshot.getValue().toString()))));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               // System.out.println(dataSnapshot.getKey() + " " + dataSnapshot.getValue());
                updateCurrentPrice(dataSnapshot.getKey(), Double.valueOf(String.format("%.2f", Double.valueOf(dataSnapshot.getValue().toString()))));

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void updateCurrentPrice(final String currencyCode, final Double price) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmWalletModel wallet = realm.where(RealmWalletModel.class).equalTo("currencyCode", currencyCode).findFirst();
                if(wallet != null) {
                    wallet.setCurrentPrice(price);
                    realm.insertOrUpdate(wallet);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                RealmQuery<RealmWalletModel> walletQuery = realm.where(RealmWalletModel.class);
                RealmResults<RealmWalletModel> walletQueryResult = walletQuery.findAll();

                Iterator<RealmWalletModel> walletModelIterator = walletQueryResult.iterator();
                Double currentBalance = 0.0;
                while (walletModelIterator.hasNext()) {
                    RealmWalletModel currentWallet = walletModelIterator.next();
                    currentBalance += currentWallet.getCurrentPrice() * currentWallet.getBalance();
                }

                if (!walletQueryResult.isEmpty()) {


                    final Double finalCurrentBalance = currentBalance;
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmBalanceModel balanceModel = realm.where(RealmBalanceModel.class).findFirst();
                            if(balanceModel != null) {
                                balanceModel.setCurrentBalance(finalCurrentBalance);

                            } else {
                                balanceModel = realm.createObject(RealmBalanceModel.class);
                                balanceModel.setCurrentBalance(finalCurrentBalance);
                            }
                                realm.insertOrUpdate(balanceModel);
                        }
                    }, null, null);

                }

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
            }
        });
    }

    private void initializeAndListenHistorical(final String historicalIndex) {
        DatabaseReference historicalPriceRef = database.getReference("historicalPricing/" + baseCurrencyCode  + "/" + historicalIndex);
        historicalPriceRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String currencyCode = dataSnapshot.getKey();
                DatabaseReference currencyCodeRef = database.getReference("historicalPricing/" + baseCurrencyCode  + "/" + historicalIndex + "/" + currencyCode);
                currencyCodeRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        System.out.println(historicalIndex);
                        updateHistoricalPrice(historicalIndex, currencyCode, Float.valueOf(dataSnapshot.getKey().replace(':', '.')), Float.valueOf(dataSnapshot.getValue().toString()));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        updateHistoricalPrice(historicalIndex, currencyCode, Float.valueOf(dataSnapshot.getKey().replace(':', '.')), Float.valueOf(dataSnapshot.getValue().toString()));

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println(historicalIndex + " " + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateHistoricalPrice(final String historicalIndex, final String currencyCode, final Float timestamp, final Float price) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmHistoricalPriceModel historicalPrice = realm.where(RealmHistoricalPriceModel.class).equalTo("historicalIndex", historicalIndex).equalTo("currencyCode", currencyCode).equalTo("timeStamp", timestamp).findFirst();
                if(historicalPrice == null) {
                    historicalPrice =  realm.createObject(RealmHistoricalPriceModel.class);
                    historicalPrice.setCurrencyCode(currencyCode);
                    historicalPrice.setTimeStamp(timestamp);
                    historicalPrice.setHistoricalIndex(historicalIndex);
                    historicalPrice.setPrice(price);

                } else {
                    historicalPrice.setPrice(price);
                }
                System.out.println("Saved " + historicalIndex + " " + currencyCode + " " + timestamp);
                realm.insertOrUpdate(historicalPrice);
            }
        }, null, null);
    }
}
