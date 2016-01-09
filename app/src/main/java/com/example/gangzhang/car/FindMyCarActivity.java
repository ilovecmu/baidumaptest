package com.example.gangzhang.car;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;


import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnTrackListener;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/*
                        zoom 1.0 2000km 20.0
                        zoom 2.0 2000km 10.0
                        zoom 3.0 2000km 5.0
                        zoom 4.0 1000km 2.50
                        zoom 5.0 500km 1.25
                        zoom 6.0 200km 0.64
                        zoom 7.0 100km 0.32
                        zoom 8.0 50km 0.16
                        zoom 9.0 25km 0.08
                        zoom 10.0 20km 0.04
                        zoom 11.0 10km 0.02
                        zoom 12.0 5km 0.01
                        zoom 13.0 2km 0.005
                        zoom 14.0 1km 0.002
                        zoom 15.0 500m 0.001
                        zoom 16.0 200m 0.0007
                        zoom 17.0 100m 0.0003
                        zoom 18.0 50m 0.00015
                        zoom 19.0 25m 0.000075
                        zoom 20.0 10m 0.000038
                        zoom 21.0 5mm 0.000019

                        * */
public class FindMyCarActivity extends Activity {
    private MapView mMapView;
    private static BaiduMap mBaiduMap;
    private LBSTraceClient client;
    private BitmapDescriptor mCurrentMarker;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private final static String KEY_LATITUDE = "latitude";
    private final static String KEY_LONGITUDE = "LONGITUDE";
    final private int THREAD_STATE_PAUSE = 0;
    final private int THREAD_STATE_STOP = 1;
    final private int THREAD_STATE_RUN = 2;
    private int mThreadState = THREAD_STATE_RUN;
    private static LatLng mStartPoint;
    private Context context;
    private  LocationClient mLocClient;
    private ImageButton mImageButtonLocation;
    private ImageButton mImageButtonRoute;
    private MyLocationData mCurrentLocation ;
    private OverlayManager mOverlay;
    private ImageView mLoadingImage;
    private Animation operatingAnim;

    private View mapBubble;
    private static TextView mSpeedView;
    private static BitmapDescriptor mBitmap;

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
    private RoutePlanSearch mSearch =null;

    private  final Handler handler = new MyHandler(this) ;
    private static class MyHandler extends Handler{
        private final WeakReference<Activity> mActivity;
        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    addLocationToMap(msg);
                    break;
                default:
                    break;
            }
        }
    }

    boolean isFirstLoc = true;// 是否首次定位


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_my_car);


        context = this;
        mLoadingImage = (ImageView)findViewById(R.id.loading);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        initMap();
        client = new LBSTraceClient(this);

        mapBubble = LayoutInflater.from(this).inflate(R.layout.map_bubble, new LinearLayout(this),false);
        mSpeedView = (TextView)mapBubble.findViewById(R.id.speed);
        mSearch = RoutePlanSearch.newInstance();

        mImageButtonLocation = (ImageButton)findViewById(R.id.location);
        mImageButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
                LatLng ll = new LatLng( mBaiduMap.getLocationData().latitude,  mBaiduMap.getLocationData().longitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                if(mOverlay!=null){
                    mOverlay.removeFromMap();
                    mOverlay = null;
                }

            }
        });
        mImageButtonRoute = (ImageButton)findViewById(R.id.route);
        mImageButtonRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocClient.start();
            }
        });
        initActionBar();
        initLocation();
        startTrack();
        initAnim();
//
//        MapView layout = (MapView)findViewById(R.id.bmapView);
//        layout.setEnabled(false);
//        layout.setClickable(false);
//        layout.setActivated(false);
//        mImageButtonLocation.setClickable(false);
//        mImageButtonLocation.setEnabled(false);



    }

     @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("myapp", "onDestroy");
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSearch.destroy();
        mBaiduMap = null;
        mThreadState = THREAD_STATE_STOP;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("myapp", "onResume");

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        mThreadState = THREAD_STATE_RUN;

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("myapp", "onPause");

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        mThreadState = THREAD_STATE_PAUSE;

    }

    private  void showWalkingRouteLine(WalkingRouteResult result){
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            Log.d("myapp", "WalkingRouteResult");
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            mOverlay = overlay;
        }
    }
    private  void showDriveringRouteLine(DrivingRouteResult result){
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            mOverlay = overlay;
        }
    }

    private void initMap(){
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
        mBaiduMap.setMapStatus(msu);

        BDLocation location = new BDLocation();
        location.setLatitude(31.3);
        location.setLongitude(120.5);

        final MyLocationData locData = new MyLocationData.Builder()
                .accuracy(51)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();

        mBaiduMap.setMyLocationData(locData);

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
        mStartPoint = ll;
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.taxi_bearing);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
    }
    private void initLocation(){
        //  定位初始化
        MyLocationListenner myListener = new MyLocationListenner();
        myListener.setCallBack(new LocationInterface() {
            @Override
            public void onLocationInterface(BDLocation location) {

                Log.d("myapp", "reoute trace");
                LatLng stLng = new LatLng(location.getLatitude(), location.getLongitude());
                final PlanNode stNode = PlanNode.withLocation(stLng);
                final PlanNode enNode = PlanNode.withLocation(mStartPoint);


                OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
                    public void onGetWalkingRouteResult(WalkingRouteResult result) {
                        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                            Toast.makeText(FindMyCarActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                        }
                        stopLoading();
                        showWalkingRouteLine(result);
                    }

                    public void onGetTransitRouteResult(TransitRouteResult result) {
                        //获取公交换乘路径规划结果
                        stopLoading();

                        Log.d("myapp", "onGetTransitRouteResult");
                    }

                    public void onGetDrivingRouteResult(DrivingRouteResult result) {
                        //获取驾车线路规划结果
                        Log.d("myapp", "onGetTransitRouteResult");
                        stopLoading();

                        showDriveringRouteLine(result);
                    }
                };

                mSearch.setOnGetRoutePlanResultListener(listener);
                final Bundle bd = new Bundle();
                final String[] mrouteWay = new String[]{"步行", "驾车"};
                new AlertDialog.Builder(context).setTitle("单选框").setIcon(
                        android.R.drawable.ic_dialog_info).setSingleChoiceItems(
                        mrouteWay, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                bd.putInt("which", which);
                                dialog.dismiss();
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        startLoading();

                        switch (bd.getInt("which")) {
                            case 0:
                                mSearch.walkingSearch((new WalkingRoutePlanOption())
                                        .from(stNode)
                                        .to(enNode));
                                break;
                            case 1:
                                mSearch.drivingSearch((new DrivingRoutePlanOption())
                                        .from(stNode)
                                        .to(enNode));
                                break;
                        }
                    }
                }).show();
            }
        });
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        mLocClient.setLocOption(option);
    }

    private void initActionBar(){
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        final DatePickerDialog dialog =new DatePickerDialog(FindMyCarActivity.this, null,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                int month = dialog.getDatePicker().getMonth();
                int year = dialog.getDatePicker().getYear();
                int day = dialog.getDatePicker().getDayOfMonth();
                DateSet.onDateSet(null, year, month, day);


            }
        });
        dialog.setCanceledOnTouchOutside(false);
        ActionBar actionBar = getActionBar();
        if(actionBar !=null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            View actionbarLayout = LayoutInflater.from(this).inflate( R.layout.actionbar_findcar, new FrameLayout(this),false);
            actionBar.setCustomView(actionbarLayout);
            Button bt = (Button)actionbarLayout.findViewById(R.id.history_track);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                }
            });

        }
    }
    private void initAnim(){
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.loading_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private void startTrack(){
        new Thread(new Runnable() {
            int n = 0;

            @Override
            public void run() {
                while (true) {
                    if (mThreadState == THREAD_STATE_RUN) {

                        Bundle bd = new Bundle();
                        bd.putDouble(KEY_LATITUDE, 31.3 + 0.0001 * n);
                        bd.putDouble(KEY_LONGITUDE, 120.5);
                        Message msg = new Message();
                        msg.what = 0;
                        msg.setData(bd);
                        handler.sendMessage(msg);
                        n++;
                        if (n > 100) n = 0;
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {

                        }
                    } else if (mThreadState == THREAD_STATE_PAUSE) {
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e) {
                        }
                    } else {
                        break;
                    }
                }
            }
        }).start();
    }

    private  static void addLocationToMap(Message msg){
        Bundle bd = msg.getData();
        double latitude = bd.getDouble(KEY_LATITUDE);
        double longitude = bd.getDouble(KEY_LONGITUDE);

        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(latitude)
                .longitude(longitude).build();
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(latitude, longitude);
            double zoom = mBaiduMap.getMapStatus().zoom;
            double bili = (int) (2000000 / Math.pow(2, (int) zoom - 3));
            double dis = DistanceUtil.getDistance(ll, mStartPoint);
            double dis_screen = dis / bili;
//                        Log.d("myapp", "mBaiduMap.getMapStatus().dis =" + dis);
//                        Log.d("myapp", "mBaiduMap.getMapStatus().dis_screen =" + dis_screen);

            if (dis_screen > 3.0) {
                mStartPoint = new LatLng(latitude, longitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mStartPoint);
                mBaiduMap.animateMapStatus(u);
            }

            LatLng lltext = new LatLng(latitude + 7.0f / (Math.pow(2, mBaiduMap.getMapStatus().zoom - 3)), longitude);
            mSpeedView.setText("速度 120km/h");

            InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    Log.d("myapp","onInfoWindowClick");
                }
            };
            InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(mSpeedView), ll, -47, listener);
            mBaiduMap.showInfoWindow(mInfoWindow);

//                        mBaiduMap.getProjection().toScreenLocation(lltext);


        }
    }
    private void startLoading(){
        mLoadingImage.setVisibility(View.VISIBLE);
        mLoadingImage.startAnimation(operatingAnim);

    }
    private void stopLoading(){
        mLoadingImage.clearAnimation();
        mLoadingImage.setVisibility(View.INVISIBLE);

    }

    final DatePickerDialog.OnDateSetListener  DateSet = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month,
                              int day) {
            month = month+1;

            final String time  = year+"-"+month+"-"+day+" "+"00:00:00";
            final String title = year+"－"+month+"－"+day;
            Date date;
//            Log.d("myapp","DatePicker = "+view);
            try {
                //鹰眼服务ID
                long serviceId = 105088;
                //entity标识
                String entityName = "mycar";
                //是否返回精简的结果（0 : 将只返回经纬度，1 : 将返回经纬度及其他属性信息）
                int simpleReturn = 0;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);

                date = format.parse(time);
                long timeStamp = date.getTime()/1000;//开始时间（Unix时间戳）

                int startTime = (int) (timeStamp);
                //结束时间（Unix时间戳）
                int endTime = (int)( timeStamp+24*60*60);
                Log.d("myapp","endTime="+endTime);
                //分页大小
                int pageSize = 5000;
                //分页索引
                int pageIndex = 1;
                //轨迹查询监听器
                OnTrackListener trackListener = new OnTrackListener() {
                    //请求失败回调接口
                    @Override
                    public void onRequestFailedCallback(String arg0) {
                        Log.d("myapp","track请求失败回调接口消息 : " + arg0);
                    }

                    // 查询历史轨迹回调接口
                    @Override
                    public void onQueryHistoryTrackCallback(String arg0) {
                        stopLoading();
                        Log.d("myapp", "查询历史轨迹回调接口消息 : " + arg0);
                        Bundle bd  = new Bundle();
                        bd.putString("KEY_TRACK", arg0);
                        bd.putString("TIME",title);
                        Intent it = new Intent(context,HistoryTrackActivity.class);
                        it.putExtras(bd);
                        startActivity(it);
                    }

                };

                Log.d("myapp", "client");
                startLoading();
                client.queryHistoryTrack(serviceId, entityName, simpleReturn, startTime, endTime, pageSize, pageIndex, trackListener);
            }catch (Exception e){

            }
        }
    };
    public class MyLocationListenner implements BDLocationListener {

        LocationInterface cb ;

        @Override
        public void onReceiveLocation(BDLocation location) {

            Log.d("myapp","onReceiveLocation");
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;

            mCurrentLocation= new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            Log.d("myapp", "location.getLocType=" + location.getLocType());

            Log.d("myapp", "location.getRadius()=" + location.getRadius());
            Log.d("myapp", "location.getLatitude()=" + location.getLatitude());
            Log.d("myapp", "location.getLongitude()=" + location.getLongitude());
            if(cb!=null){
                cb.onLocationInterface(location);
            }
            mLocClient.stop();
        }
        public void setCallBack(LocationInterface cb){
            this.cb = cb;
        }
    }
}

