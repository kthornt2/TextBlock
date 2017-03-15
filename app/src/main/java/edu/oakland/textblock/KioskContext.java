package edu.oakland.textblock;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * Created by Remzorz on 3/10/2017.
 */

public class KioskContext extends Application {

        public KioskContext KiostInstance;
        private PowerManager.WakeLock wakeLock;
        private OnScreenOffReciever mOnScreenOffReciever;


        @Override
        public void onCreate() {
            super.onCreate();
            KiostInstance = this;
            screenOffReciever();
            startKioskService();
        }

    private void startKioskService() { // ... and this method
        startService(new Intent(this, PretendKiosk.class));
    }

        private void screenOffReciever() {
            // register screen off receiver
            final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            mOnScreenOffReciever = new OnScreenOffReciever();
            registerReceiver(mOnScreenOffReciever, filter);
        }



        //public void stopKioskService() {stopService(new Intent(this, PretendKiosk.class));}


        public PowerManager.WakeLock getWakeLock() {
            if(wakeLock == null) {
                //gets screen status from power manager
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                //will not work with API's over 23
                wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
            }
            return wakeLock;
        }

        public void closeAppContext(){

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);


        }

    }





