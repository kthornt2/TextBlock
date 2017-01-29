package edu.oakland.textblock;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import fly.speedmeter.grub.GpsDataHandler;

public class GpsServices extends Service implements LocationListener, GpsStatus.Listener {
    private LocationManager mLocationManager;

    Location lastLocation = new Location("last");
    GpsDataHandler data;



    double currentLon=0 ;
    double currentLat=0 ;
    double lastLon = 0;
    double lastLat = 0;

    PendingIntent contentIntent;


    @Override
    public void onCreate() {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0);

        updateNotification(false);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.addGpsStatusListener( this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
    }

    public void onLocationChanged(Location location) {

        //Note that normally this should run from Main activity.  Once we get to the point where our
        //main activity is fleshed out, we'll use the commented part instad of initiating a new object
        //right from the GPSDataHandler class.
        //GpsDataHandler = MainActivity.getGpsDataHandler();

        GpsDataHandler gpsDataHandler = new GpsDataHandler();
            currentLat = location.getLatitude();
            currentLon = location.getLongitude();

            lastLocation.setLatitude(lastLat);
            lastLocation.setLongitude(lastLon);
            double distance = lastLocation.distanceTo(location);

            if (location.getAccuracy() < distance){
                gpsDataHandler.distanceOutputFunction(distance);

                lastLat = currentLat;
                lastLon = currentLon;
            }

            if (location.hasSpeed()) {
                gpsDataHandler.currentSpeed(location.getSpeed());
                if(location.getSpeed() == 0){
                    new isStillStopped().execute();
                }
            }
            gpsDataHandler.update();
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
                while (GpsDataHandler.getSpeed() == 0) {
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
            GpsDataHandler.setTimeStopped(timer);
        }
    }
}
