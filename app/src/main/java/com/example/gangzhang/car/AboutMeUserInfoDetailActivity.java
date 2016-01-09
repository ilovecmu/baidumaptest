package com.example.gangzhang.car;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutMeUserInfoDetailActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView listview;
    private int mResId ;
    private LayoutInflater inflater;
    private final int RES_TYPE_ICON=R.layout.aboutme_userinfo_setting_detail_item_icon;
    private final int RES_TYPE_OTHER = R.layout.aboutme_userinfo_setting_detail_item;
    private final String[] mStringItem = new String[]{"头像","姓名","性别","简介"};
    private  final String[] mStringItemContent = new String[]{"touxiang","xxoxx","男","我爱洗澡"};


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        switch (i){
            case 0:
                break;
            case 1:
            case 3:
                final EditText inputServer = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("姓名").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mStringItemContent[i] = inputServer.getText().toString();
                    }
                });
                builder.show();
                break;
            case 2:
                final String[] items = new String[]{"男", "女"};
                new AlertDialog.Builder(this).setTitle("性别").setSingleChoiceItems(items,0,
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).setOnCancelListener(null).show();
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutme_userinfo_setting_detail);
        listview = (ListView)findViewById(R.id.user_setting_detail);
        listview.setOnItemClickListener(this);
        inflater = LayoutInflater.from(this);
        listview.setAdapter(new MyArrayAdapter());

        RelativeLayout actionbar_layout = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.actionbar_aboutme_detail,new LinearLayout(this),false);
        ActionBar mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(actionbar_layout);

        }
    }

    class MyArrayAdapter extends BaseAdapter {
        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if(position==0) return 0;
            else return 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            Log.d("myapp", "getCount=" + mStringItem.length);
            return mStringItem.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder1 vh1 = null;
            ViewHolder2 vh2 = null;
            if(convertView==null){
                if(position==0) {
                    convertView = inflater.inflate(RES_TYPE_ICON, parent, false);
                    vh1 = new ViewHolder1();
                    vh1.text = (TextView)convertView.findViewById(R.id.setting_item_text);
                    vh1.imageView = (ImageView)convertView.findViewById(R.id.setting_item_content);
                    convertView.setTag(vh1);

                }else{
                    convertView = inflater.inflate(RES_TYPE_OTHER, parent, false);
                    vh2 = new ViewHolder2();
                    vh2.text1 = (TextView)convertView.findViewById(R.id.setting_item_text);
                    vh2.text2 = (TextView)convertView.findViewById(R.id.setting_item_content);
                    convertView.setTag(vh2);
                }
            }else {
                if(position==0){
                    vh1 = (ViewHolder1)convertView.getTag();
                }else{
                    vh2 = (ViewHolder2)convertView.getTag();
                }
            }
            if(position==0){
                vh1.text.setText( mStringItem[0]);
            }else{
                vh2.text1.setText(mStringItem[position]);
                vh2.text2.setText(mStringItemContent[position]);

            }
            return convertView;
        }
        class ViewHolder1{
            TextView text;
            ImageView imageView;
        }
        class ViewHolder2{
            TextView text1;
            TextView text2;
        }
    }
}
