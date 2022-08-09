package com.sdmc.stbinterfacedetecttool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ResetBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "ResetBroadcastReceiver";
    public static String mResetKey = "fail";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("com.sdmc.action.recovery")){
            mResetKey = "pass";
            Log.i(TAG,"result : resetKeyTest test successful");
        }
    }
}
