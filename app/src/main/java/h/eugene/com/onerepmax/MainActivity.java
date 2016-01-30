package h.eugene.com.onerepmax;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import h.eugene.com.onerepmax.adapters.MainPagerAdapter;
import h.eugene.com.onerepmax.modelsdata.OneRepMax;

public class MainActivity extends AppCompatActivity implements
        FragmentMaxWeights.FragmentCallback,
        FragmentHistory.FragmentCallback {

    static final int CHANGE_SETTING = 1;  // The request code for settings
    static final int SAVE_LIFT = 2;  // The request code for settings

    public static final String LIFT_WEIGHT_LIFTED = "tag_weight_lifted";
    public static final String LIFT_REPS_PERFORMED = "tag_reps_performed";
    public static final String LIFT_ONE_REP_MAX = "tag_one_rep_max";

    private MainPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiatePagerAdapter(false);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] saveSelected = ((FragmentMaxWeights) mSectionsPagerAdapter.getActiveFragment(mViewPager, 0)).getWeight();
                if (saveSelected[0] != null && saveSelected[1] != null && saveSelected[2] != null) {
                    Intent intentSaveLift = new Intent(MainActivity.this, SaveLiftActivity.class);
                    Bundle bundleLift = new Bundle();
                    bundleLift.putString(LIFT_WEIGHT_LIFTED, saveSelected[0]);
                    bundleLift.putString(LIFT_REPS_PERFORMED, saveSelected[1]);
                    bundleLift.putString(LIFT_ONE_REP_MAX, saveSelected[2]);
                    intentSaveLift.putExtras(bundleLift);
                    startActivityForResult(intentSaveLift, SAVE_LIFT);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.enter_weight_lifted), Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intentSettings, CHANGE_SETTING);
            }
        });
    }

    private void initiatePagerAdapter(boolean saved) {
        mSectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setOffscreenPageLimit(3);
        if (saved) {
            Toast.makeText(this, "Lift Saved", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * History Item Clicked: Pass data to FragmentMaxWeights
     * From {@link FragmentHistory}
     * To {@link FragmentMaxWeights}
     *
     * @param oneRepMax the history item clicked
     */
    @Override
    public void historyClicked(OneRepMax oneRepMax) {
        int reps = Integer.valueOf(oneRepMax.getRepsPerformed()) - 2;
        ((FragmentMaxWeights) mSectionsPagerAdapter.getActiveFragment(mViewPager, 0)).fromHistory(oneRepMax.getWeightLifted(), reps);
        mViewPager.setCurrentItem(0);
    }

    /**
     * Hide and show FAB based on scroll up or down
     * From {@link FragmentMaxWeights}
     */
    @Override
    public void slideUp() {
        fab.show();
    }

    @Override
    public void slideDown() {
        fab.hide();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_SETTING) {
            if (resultCode == RESULT_OK) {
                initiatePagerAdapter(false);
            }
        } else if (requestCode == SAVE_LIFT) {
            if (resultCode == RESULT_OK) {
                initiatePagerAdapter(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }
}
