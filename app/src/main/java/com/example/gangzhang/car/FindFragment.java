package com.example.gangzhang.car;


import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class FindFragment extends Fragment {
    private Activity mContext;
    private ViewPager pager;
    private ActionBar mActionBar;
      private int oldCheckID;
    private int perWidth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);


        View view  = layoutInflater.inflate(R.layout.fragment_find,new LinearLayout(mContext),false);

        pager = (ViewPager)view.findViewById(R.id.viewpager);

        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("myapp", "onActivityCreated");

        super.onActivityCreated(savedInstanceState);

        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(lp);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final LinearLayout actionbar_layout = (LinearLayout)layoutInflater.inflate(R.layout.actionbar_find, ll, false);
        final RadioGroup indicator_text = (RadioGroup)actionbar_layout.findViewById(R.id.indicator_text);
        final ImageView indicator_img = (ImageView)actionbar_layout.findViewById(R.id.indicator_img);
//        actionbar_layout.measure(0,0);
//        Log.d("myapp","actionbar_layout.getMeasuredWidth()="+indicator_text.getMeasuredWidth());

        final ViewTreeObserver vto = indicator_text.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int height = indicator_text.getMeasuredHeight();
                int width = indicator_text.getMeasuredWidth();
                perWidth = width/3;
                indicator_text.getViewTreeObserver().removeOnPreDrawListener(this);
                indicator_img.setLayoutParams(new LinearLayout.LayoutParams(perWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                return true;
            }
        });
        oldCheckID = 0;
        indicator_text.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int j = 0;
                switch (i) {
                    case R.id.luntan:
                        j = 0;
                        break;
                    case R.id.chedui:
                        j = 1;
                        break;
                    case R.id.hudong:
                        j = 2;
                        break;
                }
                Log.d("myapp", "onCheckedChanged" + j);
                TranslateAnimation animation = new TranslateAnimation(oldCheckID * perWidth, perWidth * j, 0f, 0f);
                animation.setInterpolator(new LinearInterpolator());
                animation.setDuration(100);
                animation.setFillAfter(true);
                // 执行位移动画
                indicator_img.startAnimation(animation);
                oldCheckID = j;
                pager.setCurrentItem(j);

            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("myapp", "posit=" + position);
//                indicator_text.getChildAt(position).setSelected(true);
                int j = 0;
                switch (position) {
                    case 0:
                        j = R.id.luntan;
                        break;
                    case 1:
                        j = R.id.chedui;
                        break;
                    case 2:
                        j = R.id.hudong;
                        break;
                }
                indicator_text.check(j);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pager.setAdapter(new MyPageViewAdapter(getChildFragmentManager()));


        indicator_text.check(R.id.luntan);

        mActionBar = mContext.getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(actionbar_layout);

        }
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("myapp", "onAttach");
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("myapp", "sss=" + pager.getWidth());
        Log.d("myapp", "fff=" + pager.getMeasuredWidth());

    }


    class MyPageViewAdapter extends FragmentPagerAdapter {

        public MyPageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Fragment ff = null;
            switch (position){
                case 0:
                   ff = new FindFragmentForum();
                    break;
                case 1:
                    ff = new FindFragmentTeam();
                    break;
                case 2:
                    ff = new FindFragmentAction();
                    break;

            }

            return ff;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}