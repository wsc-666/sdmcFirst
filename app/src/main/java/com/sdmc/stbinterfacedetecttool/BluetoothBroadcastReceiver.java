package com.sdmc.stbinterfacedetecttool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothBroadcastReceiver";
    private Listener mListener;

    public  BluetoothBroadcastReceiver(Listener listener){
        mListener = listener;
    }

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        switch (action) {
            case BluetoothDevice.ACTION_FOUND:
                mListener.onDeviceFound(context,intent);
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                Log.i(TAG, "onReceive : BluetoothDevice.ACTION_DISCOVERY_STARTED");
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                mListener.onDiscoveryFinished(context);
                Log.i(TAG, "onReceive : mBluetoothList is clear " );
                break;
        }
    }
    public interface Listener {
        void onDeviceFound(Context context,Intent intent);
        void onDiscoveryFinished(Context context);
    }

}
