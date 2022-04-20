package com.dogukanhan.kayitlar.activity.customer.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.customer.CustomerSaleSummaryFragment;
import com.dogukanhan.kayitlar.activity.customer.CustomerSalesFragment;
import com.dogukanhan.kayitlar.activity.customer.CustomerSpecificationsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new CustomerSpecificationsFragment();
            case 1: return new CustomerSalesFragment();
            case 2: return new CustomerSaleSummaryFragment();
            default : return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }


}