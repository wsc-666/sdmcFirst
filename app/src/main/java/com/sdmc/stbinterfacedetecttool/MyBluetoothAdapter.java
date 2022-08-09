package com.sdmc.stbinterfacedetecttool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MyBluetoothAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List mBluetoothList;

    public MyBluetoothAdapter(Context context, List list) {
        this.inflater = LayoutInflater.from(context);
        this.mBluetoothList = list;
    }

    @Override
    public int getCount() {
        return mBluetoothList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null){
            view = inflater.inflate(R.layout.bluetooth_list, null);
        }else {
            view = convertView;
        }
        String bluetooth = String.valueOf(mBluetoothList.get(position));
        String[] split = bluetooth.split(":");
        Log.i("BluetoothTest","split = " + Arrays.toString(split));
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(split[0]);
        TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
        signalStrenth.setText(String.valueOf(Math.abs(Integer.valueOf(split[2]))));
//        signalStrenth.setText(split[1]);

        return view;
    }
}
