package h.eugene.com.onerepmax.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import h.eugene.com.onerepmax.FragmentHistory;
import h.eugene.com.onerepmax.FragmentMaxWeights;
import h.eugene.com.onerepmax.FragmentPercentages;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentMaxWeights();
            case 1:
                return new FragmentPercentages();
            case 2:
                return new FragmentHistory();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Max";
            case 1:
                return "Percentages";
            case 2:
                return "History";
        }
        return null;
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return fm.findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}