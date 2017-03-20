/*
package edu.oakland.textblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

*/
/**
 * Created by Remzorz on 3/10/2017.
 *//*


public class OnScreenOffReciever extends BroadcastReceiver {

    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
            KioskContext ctx = (KioskContext) context.getApplicationContext();
            //The device shall not sleep if Kiosk mode is on
            if(isKioskModeActive(ctx)) {
                wakeUpDevice(ctx);
            }
        }
    }

    private void wakeUpDevice(KioskContext context) {
        PowerManager.WakeLock wakeLock = context.getWakeLock();
        if (wakeLock.isHeld()) {
            wakeLock.release();  //let the lock go
        }


        wakeLock.acquire();  //so we can wake the device


        wakeLock.release();  //and let it go again
    }

    private boolean isKioskModeActive(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_KIOSK_MODE, false);
    }
}

*/
