package com.sdmc.stbinterfacedetecttool;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.WIFI_SERVICE;

public class WiFiList {

    private WifiManager mWifiManager;
    private List<ScanResult> mWiFiList;
    private List ssidList;
    private Handler handler;
    private static final String TAG = "WiFiTest";

    //获取wifi列表
    public void getWifiList(final Context context){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mWifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
                openWiFi();
                scanWiFi();
                Log.i(TAG,"getWifiList : mWiFiList = " + mWiFiList);
                ssidList = new ArrayList();
                for (int i=0; i<mWiFiList.size(); i++){
                    if (!mWiFiList.get(i).SSID.isEmpty()){
                        ssidList.add(mWiFiList.get(i));
                    }
                }
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        },0,8000);


        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
//                ListView listView = (ListView)findViewById(R.id.wifi_view);
//                listView.setAdapter(new MyWiFiAdapter(getWifiList.this, ssidList));
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
        try {
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        mWifiManager.startScan();
        mWiFiList = mWifiManager.getScanResults();
        Log.i(TAG,"scanWiFi : mWiFiList = " + mWiFiList);
    }
}
