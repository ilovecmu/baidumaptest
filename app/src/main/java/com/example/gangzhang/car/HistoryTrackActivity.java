package com.example.gangzhang.car;


import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class HistoryTrackActivity extends Activity{

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private MapStatusUpdate msUpdate = null;
    // 起点图标
    private static BitmapDescriptor bmStart;
    // 终点图标
    private static BitmapDescriptor bmEnd;
    // 起点图标覆盖物
    private static MarkerOptions startMarker = null;
    // 终点图标覆盖物
    private static MarkerOptions endMarker = null;
    // 路线覆盖物
    private static PolylineOptions polyline = null;

    private String historyTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_track);
        if(savedInstanceState == null){
            Log.d("myapp","savedInstanceState");
        }
        historyTrack = getIntent().getExtras().getString("KEY_TRACK");

        mMapView = (MapView) findViewById(R.id.bmapViewHistory);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
        mBaiduMap.setMapStatus(msu);
        showHistoryTrack(historyTrack);

        ActionBar actionBar = getActionBar();
        if(actionBar !=null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            View v = LayoutInflater.from(this).inflate(R.layout.actionbar_history,new LinearLayout(this),false);

            actionBar.setCustomView(v);
            String title = getIntent().getExtras().getString("TIME");
            Log.d("myapp","time="+title);
            TextView text = ((TextView) v.findViewById(R.id.history_time));
            text.setText(title);

        }


    }


    private void showHistoryTrack(String historyTrack) {

        HistoryTrackData historyTrackData = GsonService.parseJson(historyTrack,
                HistoryTrackData.class);

        List<LatLng> latLngList = new ArrayList<LatLng>();
        if (historyTrackData != null && historyTrackData.getStatus() == 0) {
            if (historyTrackData.getListPoints() != null) {
                latLngList.addAll(historyTrackData.getListPoints());
            }

            // 绘制历史轨迹
            drawHistoryTrack(latLngList, historyTrackData.distance);

        }

    }
    private void drawHistoryTrack(final List<LatLng> points, final double distance) {
        // 绘制新覆盖物前，清空之前的覆盖物
        mBaiduMap.clear();

        if (points == null || points.size() == 0) {
            Toast.makeText(this, "当前查询无轨迹点", Toast.LENGTH_SHORT).show();
//            resetMarker();
        } else if (points.size() > 1) {

            LatLng llC = points.get(0);
            LatLng llD = points.get(points.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(llC).include(llD).build();

            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);

            bmStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);

            // 添加起点图标
            startMarker = new MarkerOptions()
                    .position(points.get(points.size() - 1)).icon(bmStart)
                    .zIndex(9).draggable(true);

            // 添加终点图标
            endMarker = new MarkerOptions().position(points.get(0))
                    .icon(bmEnd).zIndex(9).draggable(true);

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(points);

            if (null != msUpdate) {
                mBaiduMap.setMapStatus(msUpdate);
            }

            if (null != startMarker) {
                mBaiduMap.addOverlay(startMarker);
            }

            if (null != endMarker) {
                mBaiduMap.addOverlay(endMarker);
            }

            if (null != polyline) {
                mBaiduMap.addOverlay(polyline);
            }


            Toast.makeText(this, "当前轨迹里程为 : " + (int) distance + "米", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("myapp", "onDestroy");
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mBaiduMap = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("myapp", "onResume");

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("myapp", "onPause");

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}