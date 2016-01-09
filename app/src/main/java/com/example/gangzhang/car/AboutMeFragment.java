package com.example.gangzhang.car;


import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutMeFragment extends Fragment implements View.OnClickListener{
    private ImageView mUserIcon;
    private TextView mUserInfo;
    private ListView mUserSetting;
    private RelativeLayout mUserInfoLayout;
    private ListAdapter mAdapter;
    private int mResID;
    private LayoutInflater inflater;
    private Activity mContext;

    private final String[] mSettingContent = new String[]{"我的活动","我的车队","我的消息","历史轨迹","设置","关于"};
    private final int[] mSettingIcon = new int[]{R.drawable.bank,R.drawable.bar,
            R.drawable.bath,R.drawable.beauty,R.drawable.beauty,R.drawable.beauty};

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mAdapter = new MyArrayAdapter(mContext,R.layout.aboutme_setting_item);
        inflater = LayoutInflater.from(mContext);
        ActionBar actionBar = mContext.getActionBar();

        if(actionBar !=null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            View actionbarLayout = LayoutInflater.from(mContext).inflate(R.layout.actionbar_aboutme, new FrameLayout(mContext),false);
            actionBar.setCustomView(actionbarLayout);
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutme, new LinearLayout(mContext),false);
        mUserIcon = (ImageView)view.findViewById(R.id.user_icon);
        mUserInfo = (TextView)view.findViewById(R.id.user_info);
        mUserInfoLayout= (RelativeLayout)view.findViewById(R.id.user_info_layout);
        mUserInfoLayout.setOnClickListener(this);
        mUserSetting = (ListView)view.findViewById(R.id.user_setting);
        mUserSetting.setAdapter(mAdapter);
        mUserSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("myapp","position="+i);
                Log.d("myapp","view.findViewById(R.id.setting_item_text)="
                        +((TextView)view.findViewById(R.id.setting_item_text)).getText());
                switch (i){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        Intent it = new Intent(mContext,AboutMeSettingDetailActivity.class);
                        startActivity(it);
                        break;
                }

            }
        });

        return view;
    }

        @Override
    public void onClick(View view) {
        //转到设置页面
        Log.d("myapp","onclick.......");
        Intent it=new Intent(mContext,AboutMeUserInfoDetailActivity.class);
        startActivity(it);
    }


    class MyArrayAdapter extends ArrayAdapter<String>{

        public MyArrayAdapter(Context context, int resource) {
            super(context, resource);
            mResID = resource;
        }

        @Override
        public int getCount() {
            Log.d("myapp","mSettingContent.length="+mSettingContent.length);
            return mSettingContent.length;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if(convertView == null){
                convertView = inflater.inflate(mResID,parent,false);
                vh = new ViewHolder();
                vh.image = (ImageView)convertView.findViewById(R.id.setting_item_icon);
                vh.text = (TextView)convertView.findViewById(R.id.setting_item_text);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder)convertView.getTag();
            }
            vh.image.setBackgroundResource(mSettingIcon[position]);
            vh.text.setText(mSettingContent[position]);
            return convertView;
        }

        class ViewHolder{
            ImageView image;
            TextView text;
        }
    }

}