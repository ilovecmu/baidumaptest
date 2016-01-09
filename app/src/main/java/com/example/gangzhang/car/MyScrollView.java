package com.example.gangzhang.car;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyScrollView extends LinearLayout implements RadioGroup.OnCheckedChangeListener{
    private ViewPager viewPager;
    private int oldCheckID;
    private int perWidth;
    private RadioGroup radioGroup;
    private ImageView imageView;
    private int selectedTextColor;
    private int unSelectedTextColor;
    private float textSize;
    private Drawable idicatorSrc;
    private int indicatorHeight;
    private int screenWidth;
    private int screenHeight;
    private int viewWidth;
    private int viewHeight;
    private Context context;
    private List<String>title;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public void setPager(ViewPager pager){
        viewPager = pager;
    }


    public void setTitle(Context context,List<String>title){
        this.context=context;
        this.title=title;
        int cnt  = title.size();
//        int actionbarHeight= ((Activity)context).getActionBar().getHeight();
        screenWidth = 720;
        perWidth = screenWidth/cnt;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout  ll1 = (LinearLayout)layoutInflater.inflate(R.layout.test1,new LinearLayout(context),false);
        RadioGroup radioGroup = (RadioGroup)ll1.findViewById(R.id.indicator_text);
        ImageView imageView = (ImageView)ll1.findViewById(R.id.indicator_img);

        LinearLayout.LayoutParams radioButtonLayoutParams = new LinearLayout.LayoutParams
                (0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);


        for(int i=0;i < cnt;i++){
            RadioButton radioButton = new RadioButton(context);

            radioButton.setLayoutParams(radioButtonLayoutParams);
            radioButton.setId(i);
            Log.d("myapp","cnt="+title.get(i));
            radioButton.setText(title.get(i));
            radioButton.setButtonDrawable(null);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setButtonDrawable(R.color.trans);
            radioButton.setBackground(null);
            radioGroup.addView(radioButton);
        }


        radioGroup.setOnCheckedChangeListener(this);

        oldCheckID = 0;
        radioGroup.getChildAt(0).setSelected(true);
        ( (RadioButton)radioGroup.getChildAt(0)).setTextColor(selectedTextColor);
          this.addView(ll1);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

//        ViewGroup child = (ViewGroup)((ViewGroup)getChildAt(0)).getChildAt(0);
//        int cnt = child.getChildCount();
//
//        for(int i=0;i<cnt;i++){
//            child.layout(l+100*i, t, r, b);
//            Log.d("myapp", "l=" + l);
//            Log.d("myapp","t="+t);
//            Log.d("myapp","r="+r);
//
//            Log.d("myapp", "child.getLayoutParams().width()=" + child.getLayoutParams().width);
//        }
//        Log.d("myapp","cnt="+cnt);

    }

    @Override
    public void scrollTo(int x, int y) {
        Log.d("myapp", "moveToPosition" + x);
        if(x==0) {
            moveToPosition(radioGroup, 0);
        }else if(x==1) {
            moveToPosition(radioGroup, 1);
        }else if(x==2){
            moveToPosition(radioGroup,  2);
        }
        super.scrollTo(x, y);
    }

    private void moveToPosition(RadioGroup radioGroup, int i) {
        Log.d("myapp", "moveToPosition=" + i);
        Log.d("myapp", "perWidth=" + perWidth);

        TranslateAnimation animation = new TranslateAnimation(oldCheckID*perWidth,
                perWidth * i, 0f, 0f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        animation.setFillAfter(true);
        // 执行位移动画
        imageView.startAnimation(animation);
        ((RadioButton) radioGroup.getChildAt(oldCheckID)).setTextColor(unSelectedTextColor);
        ((RadioButton) radioGroup.getChildAt(i)).setTextColor(selectedTextColor);
        oldCheckID = i;
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.d("myapp", "ncfasdf=" + i);
        moveToPosition(radioGroup, i);
        viewPager.setCurrentItem(i);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Log.d("myapp", "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d("myapp","onAttachedToWindow");
        super.onAttachedToWindow();
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 720;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
                }
            }
        Log.d("myapp","specMode="+specMode);
        Log.d("myapp","measureWidth="+result);
        viewWidth = result;
        Log.d("myapp","viewWidth="+result);

        return result;
     }
    private int measureHeight(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 720;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        Log.d("myapp","measureHeight="+result);
        viewHeight = result;

        return result;
    }




}