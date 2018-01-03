package show.me.the.money.lotto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import show.me.the.money.lotto.data.DataNumber;
import show.me.the.money.lotto.fragment.NumberFragment;
import show.me.the.money.lotto.fragment.WinNumberFragment;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ListView _listNumber;

    Fragment [] _arrayFragment = {new WinNumberFragment(), new NumberFragment()};
    ArrayList<DataNumber> _arrayRandomAreaData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("당첨번호"));
        tabLayout.addTab(tabLayout.newTab().setText("번호"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {

            @Override
            public void onPageSelected(final int position) {
                Log.d("lee ", "page : " + position);
                switch (position){
                    case 0 :
                        ((WinNumberFragment)_arrayFragment[position]).onLoad();
                        break;
                    case 1:
                        if(_arrayRandomAreaData == null)
                            _arrayRandomAreaData = ((WinNumberFragment)_arrayFragment[0]).getRandomDefaultData();
                        ((NumberFragment)_arrayFragment[position]).setInit(_arrayRandomAreaData);
                        break;
                }

            }

        });

        _listNumber = (ListView) findViewById(R.id.list_number);


        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class TabPagerAdapter extends FragmentStatePagerAdapter {
        // Count number of tabs
        private int tabCount;

        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            // Returning the current tabs
//        switch (position) {
//            case 0:
//                NumberFragment tabFragment1 = new NumberFragment();
//                return tabFragment1;
//            case 1:
//                WinNumberFragment tabFragment2 = new WinNumberFragment();
//                return tabFragment2;
//            default:
//                return null;
//        }
            return _arrayFragment[position];
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
