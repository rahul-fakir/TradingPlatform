package com.trading_platform.rahulfakir.tradingplatform.Realm.RealmAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trading_platform.rahulfakir.tradingplatform.R;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmWalletModel;

import java.util.HashSet;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by rahulfakir on 7/23/17.
 */

public class WalletAdapter extends RealmRecyclerViewAdapter<RealmWalletModel, WalletAdapter.MyViewHolder> {

    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<Integer>();

    public WalletAdapter(OrderedRealmCollection<RealmWalletModel> data) {
        super(data, true);
        setHasStableIds(true);
    }

    void enableDeletionMode(boolean enabled) {
        inDeletionMode = enabled;
        if (!enabled) {
            countersToDelete.clear();
        }
        notifyDataSetChanged();
    }

    Set<Integer> getCountersToDelete() {
        return countersToDelete;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_list_row, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RealmWalletModel obj = getItem(position);
        holder.data = obj;
        //noinspection ConstantConditions
        holder.txtWalletName.setText(obj.getName());
        holder.txtCurrentPrice.setText("$" + String.format("%.2f", obj.getCurrentPrice()));
        holder.txtWalletBalanceActual.setText(String.format("%.2f", (obj.getBalance() + obj.getRoundUpBalance())) +  " " +obj.getCurrencyCode());
        holder.txtWalletBalanceNative.setText("$" + String.format("%.2f", Double.valueOf(obj.getBalance() + obj.getRoundUpBalance()) * Double.valueOf(obj.getCurrentPrice())));




    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return index;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtWalletName, txtCurrentPrice, txtWalletBalanceActual, txtWalletBalanceNative;

        public RealmWalletModel data;

        MyViewHolder(View view) {
            super(view);
            txtWalletName = (TextView) view.findViewById(R.id.txtWalletName);
            txtCurrentPrice = (TextView) view.findViewById(R.id.txtCurrentPrice);
            txtWalletBalanceActual = (TextView) view.findViewById(R.id.txtWalletBalanceActual);
            txtWalletBalanceNative = (TextView) view.findViewById(R.id.txtWalletBalanceNative);
        }
    }
}