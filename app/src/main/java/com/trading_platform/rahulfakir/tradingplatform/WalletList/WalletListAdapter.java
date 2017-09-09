package com.trading_platform.rahulfakir.tradingplatform.WalletList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trading_platform.rahulfakir.tradingplatform.R;

import java.util.List;

/**
 * Created by rahulfakir on 7/18/17.
 */

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.MyViewHolder> {

    private List<WalletListClass> walletList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtWalletName);
            genre = (TextView) view.findViewById(R.id.txtWalletBalanceActual);
            year = (TextView) view.findViewById(R.id.txtWalletBalanceNative);
        }
    }


    public WalletListAdapter(List<WalletListClass> walletList) {
        this.walletList = walletList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WalletListClass walletListClass = walletList.get(position);
        holder.title.setText(walletListClass.getTitle());
        holder.genre.setText(walletListClass.getGenre());
        holder.year.setText(walletListClass.getYear());


    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }
}