package com.trading_platform.rahulfakir.tradingplatform.MainActivity.CurrencyDashboardFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trading_platform.rahulfakir.tradingplatform.R;

/**
 * Created by rahulfakir on 7/19/17.
 */

public class CurrencyDashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency_dashboard, container, false);
    }
}
