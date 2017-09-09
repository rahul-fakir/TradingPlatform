package com.trading_platform.rahulfakir.tradingplatform.RecyclerViewTouchHandling;

import android.view.View;

/**
 * Created by rahulfakir on 7/19/17.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}