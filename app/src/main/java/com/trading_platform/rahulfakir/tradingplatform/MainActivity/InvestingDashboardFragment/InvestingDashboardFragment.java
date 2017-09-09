package com.trading_platform.rahulfakir.tradingplatform.MainActivity.InvestingDashboardFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.trading_platform.rahulfakir.tradingplatform.R;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmHistoricalPriceModel;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmWalletModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * Created by rahulfakir on 7/19/17.
 */

public class InvestingDashboardFragment extends Fragment {

    //  Realm
    private Realm realm;
    private RealmWalletModel walletModel;

    //Components
    private TextView txtRoundUpBalance;
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_investing_dashboard, container, false);
        realm = Realm.getDefaultInstance();
        RealmResults<RealmWalletModel> result = realm.where(RealmWalletModel.class).findAll();
        result.addChangeListener(walletChangeListener);
        pieChart = (PieChart) v.findViewById(R.id.pieChart);
        txtRoundUpBalance = (TextView) v.findViewById(R.id.txtRoundUpBalance);
        return v;

    }

    private final RealmChangeListener walletChangeListener = new RealmChangeListener<RealmResults<RealmWalletModel>>() {

        @Override
        public void onChange(final RealmResults<RealmWalletModel> realmWalletModel) {
            System.out.println(realmWalletModel.first().getRoundUpBalance());
            Iterator<RealmWalletModel> iterator = realmWalletModel.iterator();
            Double roundUpBalance = 0.0;
            while (iterator.hasNext()) {
                RealmWalletModel currentWallet = iterator.next();
                roundUpBalance += currentWallet.getCurrentPrice() * currentWallet.getRoundUpBalance();
            }

            txtRoundUpBalance.setText(String.format("%.2f", roundUpBalance));

            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
            yvalues.add(new PieEntry(8f, 0));
            yvalues.add(new PieEntry(15f, 1));
            yvalues.add(new PieEntry(12f, 2));
            yvalues.add(new PieEntry(25f, 3));
            yvalues.add(new PieEntry(23f, 4));
            yvalues.add(new PieEntry(17f, 5));
            PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");

            ArrayList<String> xVals = new ArrayList<String>();

            xVals.add("January");
            xVals.add("February");
            xVals.add("March");
            xVals.add("April");
            xVals.add("May");
            xVals.add("June");

            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.invalidate();
        }


    };
}
