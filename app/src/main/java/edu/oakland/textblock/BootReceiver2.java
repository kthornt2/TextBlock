package edu.oakland.textblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Remzorz on 3/10/2017.
 */
//creating a broadcast reciever for the gps so the locking mechanism can be worked on later
public class BootReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context2, Intent intent2) {
        Intent myIntent = new Intent(context2, GpsServices.class);
        context2.startActivity(myIntent);
        Log.i("Autostart","started");
    }

}
