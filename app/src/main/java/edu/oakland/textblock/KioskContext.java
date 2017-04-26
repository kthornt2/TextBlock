
package edu.oakland.textblock;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;


//Created by Remzorz on 3/10/2017.

//Managing wakelocks, IE keeping the device awake so GPS and lock are constantly running when the phone
//is in lock state.



public class KioskContext extends Application {

        public KioskContext KiostInstance;
        private PowerManager.WakeLock wakeLock;
        @Override
        public void onCreate() {
            super.onCreate();
            KiostInstance = this;

        }


    public PowerManager.WakeLock getWakeLock() {
        if(wakeLock == null) {

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
        }
        return wakeLock;
    }
    }






