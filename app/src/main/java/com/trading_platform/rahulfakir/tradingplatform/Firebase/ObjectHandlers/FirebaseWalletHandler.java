package com.trading_platform.rahulfakir.tradingplatform.Firebase.ObjectHandlers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmBalanceModel;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmWalletModel;

import java.util.HashMap;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by rahulfakir on 7/22/17.
 */

public class FirebaseWalletHandler {

    private String uid;

    //  Firebase
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    //  Realm
    private Realm realm;

    public FirebaseWalletHandler() {
        realm = Realm.getDefaultInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        uid = user.getUid();
        userRef = database.getReference("users/" + uid);
       // initializeAndListen();
        initializeAndListenMain();

    }

    private void initializeAndListenMain() {
        DatabaseReference userWalletsRef = database.getReference("users/" + uid + "/wallets");
        userWalletsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String currencyCode = dataSnapshot.getKey();
                final String walletId = dataSnapshot.child("wallet").getValue().toString();
                //updateWallet(null, "walletId", walletId);
                //updateWallet(walletId, "currencyCode", currencyCode);
                DatabaseReference walletRef = database.getReference("wallets/" + dataSnapshot.getKey() + "/" + walletId);

                walletRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> walletSnapshot = new HashMap<String, String>();
                        Iterator<DataSnapshot> snapshotIterator = dataSnapshot.getChildren().iterator();
                        while (snapshotIterator.hasNext()) {
                            DataSnapshot currentKVPair = snapshotIterator.next();
                            walletSnapshot.put(currentKVPair.getKey(), currentKVPair.getValue().toString());
                        }
                        updateWalletMain(walletId, currencyCode, walletSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    private void updateWalletMain(final String walletId, final String currencyCode, final HashMap walletData) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmWalletModel wallet = realm.where(RealmWalletModel.class).equalTo("walletId", walletId).findFirst();
                if(wallet == null) {
                    wallet = realm.createObject(RealmWalletModel.class);
                    wallet.setWalletId(walletId);
                    wallet.setCurrencyCode(currencyCode);
                    wallet.setRoundUpBalance(Double.valueOf(walletData.get("roundUpBalance").toString()));
                    wallet.setName(walletData.get("name").toString());
                    wallet.setBalance(Double.valueOf(walletData.get("balance").toString()));
                } else {
                    wallet.setName(walletData.get("name").toString());
                    wallet.setBalance(Double.valueOf(walletData.get("balance").toString()));
                    wallet.setRoundUpBalance(Double.valueOf(walletData.get("roundUpBalance").toString()));
                }

                realm.insertOrUpdate(wallet);
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
                    currentBalance += currentWallet.getCurrentPrice() * currentWallet.getRoundUpBalance();
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


}


