package com.sdmc.stbinterfacedetecttool;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

import java.util.List;

public class BluetoothInfo {

    private static final String TAG = "BluetoothInfoTest";

    public List<String> getBluetoothList(Intent intent, List<String> bluetoothList, List<String> strList){
        BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (scanDevice != null && scanDevice.getName() != null){
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(scanDevice.getName()+":"+scanDevice.getAddress().replace(":",""));
            if (strList.indexOf(stringBuffer.toString()) == -1){
                strList.add(stringBuffer.toString());
                stringBuffer.append(":"+intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                bluetoothList.add(stringBuffer.toString());
            }
        }
        Log.i(TAG, "onCreate : bluetoothList = " + bluetoothList);
        return bluetoothList;
    }
}
