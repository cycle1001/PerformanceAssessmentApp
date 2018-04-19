package net.performance.assessment.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 *
 */
public class PunchTabsAdapter extends FragmentPagerAdapter {

    private ViewPager mViewPager;
    private Fragment[] mFragments;
    private String[] mTabTitles;

    public PunchTabsAdapter(FragmentManager fm, ViewPager viewPager, Fragment[] fragments ) {
        super( fm );
        this.mViewPager = viewPager;
        this.mFragments = fragments;
    }

    public PunchTabsAdapter(FragmentManager fm, ViewPager viewPager, Fragment[] fragments , String[] tabTitles ) {
        super( fm );
        this.mViewPager = viewPager;
        this.mFragments = fragments;
        this.mTabTitles = tabTitles;
    }

    @Override
    public Fragment getItem( int position ) {
        return mFragments[ position ];
    }

    @Override
    public int getCount( ) {
        return mFragments != null ? mFragments.length : 0;
    }

    @Override
    public CharSequence getPageTitle( int position ) {
        if ( mTabTitles != null )
        {
             return mTabTitles[ position ];
        }
        return "";
    }
}
