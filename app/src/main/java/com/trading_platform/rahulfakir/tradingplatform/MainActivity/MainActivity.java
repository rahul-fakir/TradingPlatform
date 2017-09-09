package com.trading_platform.rahulfakir.tradingplatform.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.trading_platform.rahulfakir.tradingplatform.Firebase.ObjectHandlers.FirebaseBankAccountHandler;
import com.trading_platform.rahulfakir.tradingplatform.Firebase.ObjectHandlers.FirebaseHistoricalPriceHandler;
import com.trading_platform.rahulfakir.tradingplatform.Firebase.ObjectHandlers.FirebaseWalletHandler;
import com.trading_platform.rahulfakir.tradingplatform.MainActivity.CurrencyDashboardFragment.CurrencyDashboardFragment;
import com.trading_platform.rahulfakir.tradingplatform.MainActivity.InvestingDashboardFragment.InvestingDashboardFragment;
import com.trading_platform.rahulfakir.tradingplatform.MainActivity.SummaryDashboardFragment.SummaryDashboardFragment;
import com.trading_platform.rahulfakir.tradingplatform.R;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    //  Realm
    private Realm realm;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //  Data Handlers
    FirebaseWalletHandler firebaseWalletHandler;
    FirebaseHistoricalPriceHandler firebaseHistoricalPriceHandler;
    FirebaseBankAccountHandler firebaseBankAccountHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handlers
        firebaseWalletHandler = new FirebaseWalletHandler();
        firebaseHistoricalPriceHandler = new FirebaseHistoricalPriceHandler();
        firebaseBankAccountHandler = new FirebaseBankAccountHandler();
        //  DB
        realm = Realm.getDefaultInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    CurrencyDashboardFragment currencyDashboardFragment = new CurrencyDashboardFragment();
                    return currencyDashboardFragment;
                }
                case 1: {
                    SummaryDashboardFragment summaryDashboardFragment = new SummaryDashboardFragment();
                    return summaryDashboardFragment;
                }
                case 2: {
                    InvestingDashboardFragment investingDashboardFragment = new InvestingDashboardFragment();
                    return investingDashboardFragment;
                }
                default:return null;
            }
        }

        @Override
        public int getCount() {

            return 3;
        }

    }
}
