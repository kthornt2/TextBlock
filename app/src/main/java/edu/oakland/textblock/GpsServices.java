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

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
        {   public void run(){

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


        /*try{
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }
        catch(Exception ex){}*/
        try{
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex){}
        Log.v("Debug", "in on create.. 2");
       /* if (gps_enabled) {
            //updates every 10 seconds
            checkPermission(this);
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locListener);
            Log.v("Debug", "Enabled..");
        }*/
        if (network_enabled) {
            checkPermission(this);
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locListener);
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
        double calculatedSpeed = 0.0;
        double totalDistance = 0;
        boolean isSlow = false;
        long newTime;
        long oldTime = 0;
        long timeElapsed = 0;
        double doubleTimeElapsed = 0;

        @Override
        //when GPS detects a significant change of distance (45m)....
        public void onLocationChanged(Location location) {
            Log.v("Debug", "in onLocation changed..");
            if (location != null) {

                //checkPermission(getApplicationContext());
                locManager.removeUpdates(locListener);

                //String Speed = "Device Speed: " +location.getSpeed();
                lat_new = location.getLatitude();
                lon_new = location.getLongitude();
                newTime = System.currentTimeMillis();

                if (oldTime != 0) {
                     timeElapsed = (newTime - oldTime);
                    doubleTimeElapsed = timeElapsed * .001;
                }
                double distance = CalculationByDistance(lat_new, lon_new, lat_old, lon_old);
                totalDistance = totalDistance + distance;
                if (timeElapsed != 0) {
                    calculatedSpeed = distance / doubleTimeElapsed;
                } else {
                    calculatedSpeed = distance / 10;
                }

                NumberFormat formatter = new DecimalFormat("#0.000");
                Toast.makeText(getApplicationContext(),
                                "Lat is: " + lat_new + "\n Lon is: " + lon_new +
                                "\n Distance is: " + formatter.format(distance) +
                                        "\n Time Elasped is: " + doubleTimeElapsed +
                                        "\n New Time is: " + newTime +
                                        "\n Old Time is: " + oldTime +
                                "\n Total Distance is:" + formatter.format(totalDistance) +
                                "\n CalcSpeed is: " + formatter.format(calculatedSpeed) +
                                "\n getSpeed is: " + formatter.format(location.getSpeed())
                        , Toast.LENGTH_LONG).show();
                lat_old = lat_new;
                lon_old = lon_new;
                oldTime = newTime;
                sendBroadcastMessage(totalDistance, calculatedSpeed);

                if (isMyServiceRunning(PretendKiosk.class) == false) {


                    if (calculatedSpeed >= 0.5) {
                        Intent startLock = new Intent(getApplicationContext(), PretendKiosk.class);
                        startService(startLock);
                    }

                } /*else if (isMyServiceRunning(PretendKiosk.class) == true) {

                    if (calculatedSpeed <= 1.8) {
                        isSlow = true;
                        Timer timer = new Timer ();
                        TimerTask hourlyTask = new TimerTask () {
                            @Override
                            public void run () {
                                if (calculatedSpeed >= 1.8) {
                                    cancel();
                                } else {
                                    Intent stopLock = new Intent(getApplicationContext(), PretendKiosk.class);
                                    stopService(stopLock);
                                }
                            }
                        };


                        timer.schedule (hourlyTask, 0l, 1000*60*60);   // 10 minutes

                    } else {
                        isSlow = false;
                    }


                }*/
            }
        }






        /*public double totalDistance (double mdistance){
           totalDistance = totalDistance + mdistance;

            return totalDistance;

        }*/



        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    public double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
        if (lat2 != 0){
        double Radius = EARTH_RADIUS;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
        } else {
            return 0.0;
        }


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