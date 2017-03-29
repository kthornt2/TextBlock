package com.example.daryoush.mytextblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;



public class InterceptCall extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){

        try{

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);


            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context, "Ringing" , Toast.LENGTH_LONG).show();

            }
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Toast.makeText(context, "Received!", Toast.LENGTH_SHORT).show();
            }
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
                Toast.makeText(context, "Idle:", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
