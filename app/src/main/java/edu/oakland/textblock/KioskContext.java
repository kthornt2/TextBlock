
package edu.oakland.textblock;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;


//Created by Remzorz on 3/10/2017.



public class KioskContext extends Application {

        public KioskContext KiostInstance;
        private PowerManager.WakeLock wakeLock;



        @Override
        public void onCreate() {
            super.onCreate();
            KiostInstance = this;

            startKioskService();
        }

    private void startKioskService() { // ... and this method
        startService(new Intent(this, PretendKiosk.class));
    }



        //public void stopKioskService() {stopService(new Intent(this, PretendKiosk.class));}




        public void closeAppContext(){

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);


        }

    }






