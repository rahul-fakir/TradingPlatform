package com.trading_platform.rahulfakir.tradingplatform.MainActivity.SummaryDashboardFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.trading_platform.rahulfakir.tradingplatform.AccountActivity.AccountActivity;
import com.trading_platform.rahulfakir.tradingplatform.R;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmAdapters.WalletAdapter;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmBalanceModel;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmWalletModel;
import com.trading_platform.rahulfakir.tradingplatform.RecyclerViewTouchHandling.ClickListener;
import com.trading_platform.rahulfakir.tradingplatform.RecyclerViewTouchHandling.RecyclerTouchListener;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by rahulfakir on 7/19/17.
 */

public class SummaryDashboardFragment extends Fragment {

    //  Realm
    private Realm realm;

    private WalletAdapter adapter;

    //  Components
    private RecyclerView recyclerView;
    private TextView txtCurrentBalance;

    public SummaryDashboardFragment() {

    }

    private RealmBalanceModel balanceModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_summary_dashboard, container, false);
        realm = Realm.getDefaultInstance();
        txtCurrentBalance = (TextView) v.findViewById(R.id.txtCurrentBalance);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        adapter = new WalletAdapter(realm.where(RealmWalletModel.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), AccountActivity.class);
                intent.putExtra("WALLET_ID", adapter.getItem(position).getWalletId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(getActivity().getApplicationContext(),  adapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
                recyclerView.getAdapter().getItemId(position);
            }
        }));

        return v;

    }

    @Override
    public void onPause() {
        super.onPause();
        if (balanceModel != null) {
            balanceModel.removeAllChangeListeners();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (balanceModel == null) { //bad logic TODO: fix logic
            balanceModel = realm.where(RealmBalanceModel.class).findFirst();
        }
        if (balanceModel != null) {
            balanceModel.addChangeListener(changeListener);
        }
    }

    private final RealmChangeListener<RealmBalanceModel> changeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            RealmBalanceModel balanceObject = (RealmBalanceModel) o;
            txtCurrentBalance.setText("$" + String.format("%.2f", balanceObject.getCurrentBalance() ));
        }
    };




}
