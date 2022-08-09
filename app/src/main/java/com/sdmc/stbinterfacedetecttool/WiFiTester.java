package com.sdmc.stbinterfacedetecttool;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.WIFI_SERVICE;

public class WiFiTester {
    private static final String TAG = "WiFiTest";
    private WifiManager mWifiManager;
    private List<ScanResult> mWiFiList;
    private List<ScanResult> mSsidList;
    private Handler mHandler;
    private Context mContext;
    private ListView listView;

    public WiFiTester(Context context ,ListView v){
        mContext = context;
        listView = v;
    }

    //获取wifi列表
    public void getWifiList(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mWifiManager = (WifiManager)mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
                openWiFi();
                scanWiFi();
                mSsidList = new ArrayList();
                for (int i=0; i<mWiFiList.size(); i++){
                    if (!mWiFiList.get(i).SSID.isEmpty()){
                        mSsidList.add(mWiFiList.get(i));
                    }
                }
                Message msg = new Message();
                msg.obj = mSsidList;
                mHandler.sendMessage(msg);
            }
        },3,12000);
        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                listView.setAdapter(new MyWiFiAdapter(mContext, mSsidList));
            }
        };
    }

    //打开WiFi
    private void openWiFi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }

    //扫描wifi
    private void scanWiFi(){
        mWifiManager.startScan();
        mWiFiList = mWifiManager.getScanResults();
    }
}
