package edu.oakland.textblock;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;

import java.util.Timer;
import java.util.TimerTask;

public class GpsServices extends Service
{
    private LocationManager locManager;
    private LocationListener locListener = new myLocationListener();
    static final Double EARTH_RADIUS = 6371.00;

    public static String
            DISTANCE_BROADCAST = GpsServices.class.getName() + "Location Broadcast",
            EXTRA_SPEED = "extra_speed",
            EXTRA_DISTANCE = "extra_distance";



    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    private Handler handler = new Handler();
    Thread t;

    @Override
    public IBinder onBind(Intent intent) {return null;}
    @Override
    public void onCreate() {
    }
    @Override
    public void onDestroy() {}
    @Override
    public void onStart(Intent intent, int startid) {}
    public static boolean checkPermission(final Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){


        Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();

        final Runnable r = new Runnable()
        {   public void run()
        {
            Log.v("Debug", "Hello");
            location();
            handler.postDelayed(this, 5000);
        }
        };
        handler.postDelayed(r, 5000);
        return START_STICKY;
    }

    public void location(){
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        try{
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }
        catch(Exception ex){}
        try{
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex){}
        Log.v("Debug", "in on create.. 2");
        if (gps_enabled) {
            //updates every 10 seconds
            checkPermission(this);
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locListener);
            Log.v("Debug", "Enabled..");
        }
        if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,locListener);
            Log.v("Debug", "Disabled..");
        }
        Log.v("Debug", "in on create..3");
    }
    public class myLocationListener implements LocationListener
    {
        double lat_old=0.0;
        double lon_old=0.0;
        double lat_new;
        double lon_new;
        double time=10;
        double speed;
        double totalDistance = 0;
        boolean isSlow = false;

        @Override
        //when GPS detects a significant change of distance (45m)....
        public void onLocationChanged(Location location) {
            Log.v("Debug", "in onLocation changed..");
            if (location != null) {
                double distance;
                checkPermission(getApplicationContext());
                locManager.removeUpdates(locListener);

                //String Speed = "Device Speed: " +location.getSpeed();
                lat_new = location.getLongitude();
                lon_new = location.getLatitude();




                    distance = CalculationByDistance(lat_new, lon_new, lat_old, lon_old);
                    //speed = (distance / time) * 2.23694;
                    speed = location.getSpeed() *10;



                Toast.makeText(getApplicationContext(), "Distance is: "
                        + distance + "\nSpeed is: " + speed, Toast.LENGTH_SHORT).show();
                lat_old = lat_new;
                lon_old = lon_new;
                sendBroadcastMessage(distance, location.getSpeed());

                if (isMyServiceRunning(PretendKiosk.class) == false) {


                    if (speed >= 100) {
                        Intent startLock = new Intent(getApplicationContext(), PretendKiosk.class);
                        startService(startLock);
                    }

                } /*else if (isMyServiceRunning(PretendKiosk.class) == true) {

                    if (speed <= .5) {
                        isSlow = true;
                    } else {
                        isSlow = false;
                    }


                    while (isSlow = true) {

                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            private int i = 5;

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Slow for" + i + "counts of 30",Toast.LENGTH_SHORT).show();
                                if (--i < 1 && speed <= .5) {
                                    timer.cancel();
                                    Intent stopLock = new Intent(getApplicationContext(), PretendKiosk.class);
                                    stopService(stopLock);

                                } else {
                                    i = 5;
                                }


                                }
                            },30000);*/
                    //    }
                   // }


                }
            }







        public double totalDistance (double mdistance){
           totalDistance = totalDistance + (mdistance * .00062);

            return totalDistance;

        }



        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    public double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
        double Radius = EARTH_RADIUS;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }

    private void sendBroadcastMessage(double distance, double speed) {

            double roundedDistance= Math.round((distance * 100)) / 100.0d;
            double roundedSpeed= Math.round((speed * 100)) / 100.0d;
            Intent intent = new Intent(DISTANCE_BROADCAST);
            intent.putExtra(EXTRA_DISTANCE,roundedDistance);
            intent.putExtra(EXTRA_SPEED,roundedSpeed);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}