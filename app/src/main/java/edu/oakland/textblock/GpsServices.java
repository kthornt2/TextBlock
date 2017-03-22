package edu.oakland.textblock;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.content.Intent;
//for permissions
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import static java.lang.Math.abs;


//Service is a component that allows apps to run in the background even if the user switches to
//another app.  Since the app will need to keep tabs continuously, well need our class
//to extend Service.
public class GpsServices extends Service implements LocationListener, GpsStatus.Listener {

    private static final String TAG ="GpsServices";
    private LocationManager mLocationManager;
    Location lastLocation = new Location("last location");
    GpsDataHandler data;
    //current coordinates
    double currentLon=0 ;
    double currentLat=0 ;
    double lastLon = 0;
    double lastLat = 0;
    private Thread t = null;
    private Location m;
    GoogleApiClient client;
    private GpsDataHandler.gpsUpdateTrigger gpsUpdateTrigger;


    PendingIntent contentIntent;

    //Checks for GPS location permissions.  The request, if needed,
    // has to come from the Main Activity.  (TO DO)
    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }




    @Override
    public void onCreate() {

        data=new GpsDataHandler(gpsUpdateTrigger);
        data.setFirstTime(true);

        super.onCreate();
        //This will have to work off of the main activity.  Basically it is an intent to start when the
        //main activity starts
        //Intent notificationIntent = new Intent(this, MainActivity.class);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // contentIntent = PendingIntent.getActivity(
        //this, 0, notificationIntent, 0);






        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //actually checks the permissions here
        checkPermission(this);
        mLocationManager.addGpsStatusListener(this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);





    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    public void onLocationChanged(Location location) {

        //Note that normally this should run from Main activity.  Once we get to the point where our
        //main activity is fleshed out, we'll use the commented part instead of initiating a new object
        //right from the GPSDataHandler class.
        //GpsDataHandler = MainActivity.getGpsDataHandler();

        Location lastLocation = new Location("last");
        GpsDataHandler gpsDataHandler = new GpsDataHandler();



        currentLat = location.getLatitude();
        currentLon = location.getLongitude();

        //if (data.isFirstTime()){
          //  lastLat = currentLat;
          //  lastLon = currentLon;
           // data.setFirstTime(false);
       // }

        lastLocation.setLatitude(lastLat);
        lastLocation.setLongitude(lastLon);
        double distance = lastLocation.distanceTo(location);

        if (location.getAccuracy() < distance){
            data.distanceFunction(distance);

            lastLat = currentLat;
            lastLon = currentLon;
        }

        if (location.hasSpeed()) {
            data.currentSpeed(location.getSpeed() * 3.6);
            if(location.getSpeed() == 0){
                new isStillStopped().execute();
            }

        }

        if (location.getSpeed() >= 6.7) {
            startService(new Intent(this,PretendKiosk.class));

        }
        data.update();

    }






    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }



    @Override
    public void onGpsStatusChanged(int event) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

class isStillStopped extends AsyncTask<Void, Integer, String> {
    int timer = 0;
    @Override
    protected String doInBackground(Void... unused) {
        try {
            while (data.getSpeed() == 0) {
                Thread.sleep(1000);
                timer++;
            }
        } catch (InterruptedException t) {
            return ("The sleep operation failed");
        }
        return ("return object when task is finished");
    }

    @Override
    protected void onPostExecute(String message) {
        data.setTimeStopped(timer);
    }
}
}