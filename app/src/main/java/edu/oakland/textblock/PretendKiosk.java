package edu.oakland.textblock;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Remzorz on 3/10/2017.
 */

public class PretendKiosk extends Service {
    //private static final long INTERVAL = TimeUnit.SECONDS.toMillis(1); // periodic interval to check in seconds -> 1 seconds
    private static final String TAG = PretendKiosk.class.getSimpleName();
    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

    private Thread kioskThread = null;
    private Thread approvalCheckThread = null;
    private Context ctx = null;
    private boolean running = false;

    @Override
    public void onDestroy() {
        //clean up for app closing
        Log.i(TAG, "Stopping service 'LockMode'");
        running = false;

        if (kioskThread.isAlive()) {
            Log.d("MyApp", "the thread is alive. now we are going to kill it.");
            kioskThread.interrupt();
        }
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        ctx = this;
        // start a thread that periodically checks if your app is in the foreground
        kioskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                        handleKioskMode();
                        try {
                            //do the thread every half second.  I may increase this later, so the phone doesnt explode
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Log.i(TAG, "Thread interrupted: 'LockMode'");
                        }
                } while (running);
                //stop thread
               stopSelf();
            }
        }

        );

        approvalCheckThread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    checkDatabaseForApproval();
                    try {

                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Thread interrupted: 'LockMode'");
                    }
                } while (running);
                //stop thread
              stopSelf();
            }
        }

        );
        kioskThread.start();
        return Service.START_NOT_STICKY;
    }
private void checkDatabaseForApproval()
{
    final String URL_GETPHOTOS = "http://52.41.167.226/Approvals.php";
    RequestQueue requestQueue;
    requestQueue = Volley.newRequestQueue(this);

    // instantiate a StringRequest to get photos' links
    StringRequest getPhotosRequest;
    getPhotosRequest = new StringRequest(Request.Method.POST, URL_GETPHOTOS, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("MyApp Res", response);
            if(response.contains("1")){
                Log.d("PretendKiosk", "The phone was unlocked via Guardian Approval");
                Toast.makeText(getApplicationContext(), "Lock has been lifted via Guardian Approval",Toast.LENGTH_LONG);
                GpsServices.lockIsListening = false;
                GpsServices.showGPSDialogue = false;
                stopSelf();

            }
            // TODO
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("MyApp ResErr", error.toString());

        }
    }) {
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new Hashtable<String, String>();
            // add IMEI into the request
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String IMEI = telephonyManager.getDeviceId();
            Log.d("MyApp IMEI", IMEI);
            params.put("IMEI", IMEI);
            return params;
        }
    };
    requestQueue.add(getPhotosRequest);


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

