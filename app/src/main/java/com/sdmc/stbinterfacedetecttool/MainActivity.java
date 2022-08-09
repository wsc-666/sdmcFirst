package com.sdmc.stbinterfacedetecttool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "stbinterfacedetecttoolTest";
    public static String wifiList = "fail";
    public static String bluetoothList = "fail";
    public static String upKeyLong = "fail";
    public static String upKeyShort = "fail";
    private PowerBroadcastReceiver mPowerBroadcastReceiver;
    private ResetBroadcastReceiver mResetBroadcastReceiver;
    private List<String> mBluetoothList;
    private List<String> mStrList;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothBroadcastReceiver mBluetoothBroadcastReceiver;
    private ListView bluetoothListView;
    private ListView wifiListView;
    private Boolean isShortPressKey = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        getWiFiList();
        getBluetoothList();
        powerKeyTest();
        resetKeyTest();
        new LedTester(this).writeSysFile();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                isShortPressKey = false;
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提示");
                dialog.setMessage("是否恢复出厂设置");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_FACTORY_RESET);
                        intent.setPackage("android");
                        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        intent.putExtra(Intent.EXTRA_REASON,"doFactoryReset");
                        intent.putExtra(Intent.EXTRA_WIPE_EXTERNAL_STORAGE,false);
                        intent.putExtra(Intent.EXTRA_WIPE_ESIMS,true);
                        sendBroadcast(intent);
                        upKeyLong = "pass";
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                Log.i(TAG, "onKeyLongPress : isLongPressKey" );
                break;
        }
        return super.onKeyLongPress(keyCode, keyEvent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (isShortPressKey) {
                    Process p = null;
                    DataOutputStream os = null;
                    try {
                        Runtime.getRuntime().exec("am start com.android.tv.settings/.MainSettings"+ "\n");
                        upKeyShort = "pass";
                        Log.i(TAG, "onKeyUp : open tvSetting successfully" );
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i(TAG, "onKeyUp : can't open tvSetting " );
                    } finally {
                        if(p != null)  { p.destroy(); }
                        if(os != null){
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                isShortPressKey = false;
                break;
            default:
                break;
        }
        return super.onKeyUp(keyCode, keyEvent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                event.startTracking();
                Log.i(TAG, "onKeyDown : event.getRepeatCount() = " + event.getRepeatCount() );
                if (event.getRepeatCount() == 0){
                    isShortPressKey = true;
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //复位键
    public void resetKeyTest(){
        mResetBroadcastReceiver = new ResetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sdmc.action.recovery");
        registerReceiver(mResetBroadcastReceiver,filter);
    }

    //待机键
    private void powerKeyTest(){
        mPowerBroadcastReceiver = new PowerBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mPowerBroadcastReceiver,filter);
    }

    //获取蓝牙列表
    private void getBluetoothList(){
        final BluetoothInfo bluetoothInfo = new BluetoothInfo();
        mBluetoothList = new ArrayList<>();
        mStrList =  new ArrayList<>();
        BluetoothBroadcastReceiver.Listener listener = new BluetoothBroadcastReceiver.Listener() {
            @Override
            public void onDeviceFound(Context context,Intent intent) {
                bluetoothListView.setAdapter(new MyBluetoothAdapter(context,bluetoothInfo.getBluetoothList(intent,mBluetoothList,mStrList)));
            }
            @Override
            public void onDiscoveryFinished(Context context) {
                mBluetoothList.clear();
                mStrList.clear();
                bluetoothListView.setAdapter(new MyBluetoothAdapter(context,mBluetoothList));
            }
        };
        mBluetoothBroadcastReceiver = new BluetoothBroadcastReceiver(listener);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mBluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                registerReceiver(mBluetoothBroadcastReceiver,filter);
            }
        },0,1000);
        bluetoothList = "pass";
    }

    //获取WiFi列表
    private void getWiFiList(){
        wifiListView = (ListView)findViewById(R.id.wifi_view);
        bluetoothListView = (ListView)findViewById(R.id.bluetooth_view);
        new WiFiTester(this, wifiListView).getWifiList();
        wifiList = "pass";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBluetoothBroadcastReceiver);
        unregisterReceiver(mPowerBroadcastReceiver);
    }


}
