package com.sdmc.stbinterfacedetecttool;

import android.bluetooth.BluetoothAdapter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity_backup extends AppCompatActivity {

    private WifiManager mWifiManager;
    private BluetoothAdapter mBluetoothAdapter;
    private List<ScanResult> mWiFiList;
    private static final String TAG = "WiFiTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWifiList();
    }

    //获取wifi列表
    private void getWifiList(){
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mWiFiList = mWifiManager.getScanResults();
//        sortByLevel(mWiFiList);
//        String[] ssidList = new String[mWiFiList.size()];
//        List ssidList = new ArrayList();
//        Log.i(TAG,"getWifiList : mWiFiList.size() = " + mWiFiList.size());
//        for (int i=0; i<mWiFiList.size(); i++){
//            ScanResult scanResult = mWiFiList.get(i);
//            if (!scanResult.SSID.isEmpty()){
//                Log.i(TAG,"getWifiList : scanResult.SSID = " + scanResult.SSID);
////                ssidList[i] = scanResult.SSID+"   "+scanResult.level;
//                ssidList.add(scanResult.SSID+"   "+scanResult.level);
//            }
//        }
//        Log.i(TAG,"getWifiList : ssidList = " + ssidList);
//        ArrayAdapter<List> adapter = new ArrayAdapter<>(MainActivity.this,
//                android.R.layout.simple_list_item_1, ssidList);
        ListView listView = (ListView)findViewById(R.id.wifi_view);
        listView.setAdapter(new MyWiFiAdapter(this,mWiFiList));
    }

    //wifi信号强弱排序
    private void sortByLevel(List<ScanResult> list){
        Log.i(TAG,"sortByLevel : list = " + list);
        for (int i=0;i<list.size();i++){
            for (int j=1;j<list.size();j++){
                if (list.get(i).level<list.get(j).level){
                    ScanResult temp = null;
                    temp = list.get(i);
                    list.set(i,list.get(j));
                    list.set(j,temp);
                }
            }
        }

    }

    //获取蓝牙列表
    private void getBluetoothList(){

    }
}
