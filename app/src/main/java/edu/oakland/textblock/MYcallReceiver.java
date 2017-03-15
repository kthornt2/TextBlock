package com.example.daryoush.detectincomingcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by daryoush on 3/11/2017.
 */

    public class MYcallReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent){
        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Toast.makeText(context, "call from white list: "+incomingNumber, Toast.LENGTH_LONG).show();

        } else if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE) || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            Toast.makeText(context, "Detected call hangup event", Toast.LENGTH_LONG).show();
        }
    }
}
