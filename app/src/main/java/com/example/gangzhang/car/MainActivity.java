package com.example.gangzhang.car;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.baidu.mapapi.SDKInitializer;

public class MainActivity extends FragmentActivity implements CarControlFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private FrameLayout mFragmentPage;
    private RadioGroup mRadioGroup;
    private int oldId;
    private CarControlFragment cf = null;
    private AboutMeFragment af = null;
    private FindFragment ff =null;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        fm = getSupportFragmentManager();

        // Set up the ViewPager with the sections adapter.
        mFragmentPage = (FrameLayout) findViewById(R.id.pager);
//        mFragmentPage.setAdapter(mSectionsPagerAdapter);

        mRadioGroup = (RadioGroup)findViewById(R.id.main_tab);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioGroup.findViewById(oldId).setSelected(false);
                radioGroup.findViewById(i).setSelected(true);
                oldId = i;
                FragmentTransaction transaction = fm.beginTransaction();

                switch (i){

                    case R.id.control_bt:
//                        mFragmentPage.setCurrentItem(1);
                        if(cf==null) {
                            cf = new CarControlFragment();
                        }
                        transaction.replace(R.id.pager,cf);
                        break;
                    case R.id.find_bt:
                        if(ff ==null){
                            ff = new FindFragment();
                        }
                        transaction.replace(R.id.pager,ff);
                        break;
                    case R.id.me_bt:
                        if(af==null){
                            af = new AboutMeFragment();
                        }
                        transaction.replace(R.id.pager,af);
                        break;
                }
                transaction.commit();
            }
        });
        oldId = R.id.control_bt;
        mRadioGroup.check(R.id.control_bt);

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
    public void onFragmentInteraction(Uri uri){

    }

}
