package com.example.gangzhang.car;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AboutMeSettingDetailActivity extends Activity {
    private boolean isSelected = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutme_setting_detail);
        final ImageButton button =(ImageButton)findViewById(R.id.toggon_react);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelected = !isSelected;
                button.setSelected(isSelected);
            }
        });

    }

}