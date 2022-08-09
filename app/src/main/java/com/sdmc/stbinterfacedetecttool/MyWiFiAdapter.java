package com.sdmc.stbinterfacedetecttool;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyWiFiAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<ScanResult> list;
    public MyWiFiAdapter(Context context, List<ScanResult> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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

        view = inflater.inflate(R.layout.wifi_list, null);
        ScanResult scanResult = list.get(position);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(scanResult.SSID);
        TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
        signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
        return view;
    }

}
