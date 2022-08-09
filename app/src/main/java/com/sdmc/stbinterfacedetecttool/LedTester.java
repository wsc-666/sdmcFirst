package com.sdmc.stbinterfacedetecttool;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LedTester {

    private static final String TAG = "LedTester";
    private static final String SYS_PATH = "/sys/class/sdmc/sys_led";
    public static String mLedLight = null;
    private Handler mHandler;
    private Context mContext;

    public LedTester(Context context){
        mContext = context;
    }

    //Led灯测试
    public void writeSysFile(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int value = readSysFile();
                FileWriter fileWriter = null;
                BufferedWriter bufWriter = null;
                try{
                    fileWriter = new FileWriter(SYS_PATH);
                    bufWriter = new BufferedWriter(fileWriter);
                    bufWriter.write(String.valueOf(value));
                    bufWriter.close();
                    Log.i(TAG, "writeSysFile : write success to value=" + value);
                } catch (IOException e){
                    e.printStackTrace();
                    Log.e(TAG, "writeSysFile : can't write the " + SYS_PATH);
                } finally {
                    try {
                        if (null != bufWriter) {
                            bufWriter.close();
                        }
                        if (null != fileWriter) {
                            fileWriter.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        },5000,1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                mHandler.sendMessage(msg);
            }
        },5000);
        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                alertDialog();
            }
        };
    }

    private void alertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("提示");
        dialog.setMessage("请确认Led灯是否闪烁");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLedLight = "fail";
            }
        });
        dialog.show();
    }

    public int readSysFile() {
        String value = "0";
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(SYS_PATH);
            bufferedReader = new BufferedReader(fileReader);
            value = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
                if (null != fileReader) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "readSysFile : value=" + value);
        return Integer.valueOf(value);
    }
}
