package edu.oakland.textblock;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

/**
 * Created by Remzorz on 3/10/2017.
 */

public class PretendKiosk extends Service {
    //private static final long INTERVAL = TimeUnit.SECONDS.toMillis(1); // periodic interval to check in seconds -> 1 seconds
    private static final String TAG = PretendKiosk.class.getSimpleName();
    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;

    @Override
    public void onDestroy() {

        //clean up for app closing
        Log.i(TAG, "Stopping service 'LockMode'");
        running = false;
        t.interrupt();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        ctx = this;
        // start a thread that periodically checks if your app is in the foreground
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    if (running = true) {
                        handleKioskMode();
                        try {
                            //do the thread every half second.  I may increase this later, so the phone doesnt explode
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Log.i(TAG, "Thread interrupted: 'LockMode'");
                        }
                    } else {
                        break;
                    }
                } while (running);
                //stop thread
                stopSelf();
            }
        }
        );
        t.start();
        return Service.START_NOT_STICKY;
    }


    private void handleKioskMode() {
        // checks if the kiosk mode is active
        if (isKioskModeActive(this)) {
            // by looking at the list of tasks.  Is it the front task? If not...
            if (isInBackground() == true) {
                //restore the app
                restoreApp(); // restore!
            }
        }
    }

    private boolean isInBackground() {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(ctx.getPackageName())) {
                        return false;
                    }
                }
            }
        }

        return true;
        /*List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return (!ctx.getApplicationContext().getPackageName().equals(componentInfo.getPackageName()));*/
    }

    private void restoreApp() {
        // Restart activity
        Intent i = new Intent(ctx, BlockActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);

    }

    public boolean isKioskModeActive(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_KIOSK_MODE, true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

