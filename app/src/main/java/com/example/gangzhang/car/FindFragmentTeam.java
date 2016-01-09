package com.example.gangzhang.car;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FindFragmentTeam extends Fragment {
    private Activity mContext;
    private ViewPager pager;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.find_fragment_team,  new LinearLayout(mContext),false);
        listView = (ListView) view.findViewById(R.id.find_chedui_fragmetn_listview);
        listView.setAdapter(new MyAdapter());
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

      private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            return inflater.inflate(R.layout.find_team_fragment_item,viewGroup,false);
        }
    }

}