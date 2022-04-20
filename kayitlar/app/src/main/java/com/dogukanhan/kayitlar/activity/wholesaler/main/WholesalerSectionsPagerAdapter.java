package com.dogukanhan.kayitlar.activity.wholesaler.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.activity.wholesaler.WholesalerPurchaseSummaryFragment;
import com.dogukanhan.kayitlar.activity.wholesaler.WholesalerPurchasesFragment;
import com.dogukanhan.kayitlar.activity.wholesaler.WholesalerSpecificationsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class WholesalerSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_4, R.string.tab_text_5,R.string.tab_text_6};
    private final Context mContext;

    public WholesalerSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new WholesalerSpecificationsFragment();
            case 1: return new WholesalerPurchasesFragment();
            case 2: return new WholesalerPurchaseSummaryFragment();
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
        // Show 3 total pages.
        return 3;
    }


}