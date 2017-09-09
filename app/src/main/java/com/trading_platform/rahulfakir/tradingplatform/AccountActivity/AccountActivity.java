package com.trading_platform.rahulfakir.tradingplatform.AccountActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.trading_platform.rahulfakir.tradingplatform.R;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmHistoricalPriceModel;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmWalletModel;

import java.util.ArrayList;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AccountActivity extends AppCompatActivity {

    //  Realm
    private Realm realm;
    private RealmWalletModel walletModel;

    //  Components
    private TextView txtWalletName, txtBalance, txtBalanceNative;
    private LineChart lineChart;
    Button btnPastHour, btnPastDay, btnPastMonth, btnPastYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        txtWalletName = (TextView) findViewById(R.id.txtWalletName);
        txtBalance = (TextView) findViewById(R.id.txtBalance);
        txtBalanceNative = (TextView) findViewById(R.id.txtBalanceNative);

        lineChart = (LineChart) findViewById(R.id.chart);

        btnPastHour = (Button) findViewById(R.id.btnPastHour);
        btnPastDay = (Button) findViewById(R.id.btnPastDay);
        btnPastMonth = (Button) findViewById(R.id.btnPastMonth);
        btnPastYear = (Button) findViewById(R.id.btnPastYear);

        btnPastHour.setOnClickListener(getButtonListener("pastHour"));
        btnPastDay.setOnClickListener(getButtonListener("pastDay"));
        btnPastMonth.setOnClickListener(getButtonListener("pastHour"));
        btnPastYear.setOnClickListener(getButtonListener("pastDay"));


        Intent intent = getIntent();
        String walletId = intent.getStringExtra("WALLET_ID");
        realm = Realm.getDefaultInstance();
        System.out.println(walletId);

        walletModel = realm.where(RealmWalletModel.class).equalTo("walletId", walletId).findFirst();
        txtWalletName.setText(walletModel.getName());
        txtBalance.setText(String.format("%.2f", walletModel.getBalance()) + " " + walletModel.getCurrencyCode());
        txtBalanceNative.setText("$" + String.format("%.2f", Double.valueOf(walletModel.getBalance()) * Double.valueOf(walletModel.getCurrentPrice())));
        walletModel.addChangeListener(walletListener);
        RealmResults<RealmHistoricalPriceModel> result = realm.where(RealmHistoricalPriceModel.class).findAll();
        setChangeListener("pastHour");

    }

    private final RealmChangeListener<RealmWalletModel> walletListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            RealmWalletModel wallet = (RealmWalletModel) o;
            txtWalletName.setText(wallet.getName());
            txtBalance.setText(String.format("%.2f", wallet.getBalance()) + " " + wallet.getCurrencyCode());
            txtBalanceNative.setText("$" + String.format("%.2f", Double.valueOf(wallet.getBalance()) * Double.valueOf(wallet.getCurrentPrice())));
        }


    };

    private final RealmChangeListener historicalPriceListener = new RealmChangeListener<RealmResults<RealmHistoricalPriceModel>>() {

        @Override
        public void onChange(final RealmResults<RealmHistoricalPriceModel> realmHistoricalPriceModels) {
            setChartData(realmHistoricalPriceModels);
        }


    };

    private void setChartData(final RealmResults<RealmHistoricalPriceModel> realmHistoricalPriceModels) {
        RealmLineDataSet<RealmHistoricalPriceModel> lineDataSet = new RealmLineDataSet<RealmHistoricalPriceModel>(realmHistoricalPriceModels.sort("timeStamp"), "timeStamp", "price");
        lineDataSet.setDrawValues(false);

        lineDataSet.setLineWidth(2);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawAxisLine(false);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);


        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(value + "#");
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };
        //   barChart.getXAxis().setValueFormatter(formatter);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void  setChangeListener(String historicalPriceIndex) {
        RealmResults<RealmHistoricalPriceModel> historicalQuery = realm.where(RealmHistoricalPriceModel.class).equalTo("historicalIndex", historicalPriceIndex).equalTo("currencyCode", walletModel.getCurrencyCode()).findAll();
        historicalQuery.addChangeListener(historicalPriceListener);
        System.out.println(historicalQuery.first().getPrice());
        setChartData(historicalQuery);

    }

    private View.OnClickListener getButtonListener(final String historicalPriceIndex) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChangeListener(historicalPriceIndex);
            }
        };
    }
}