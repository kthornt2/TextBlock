package edu.oakland.textblock;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class GpsServices extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    public static boolean lockIsListening = true;
    public static boolean showGPSDialogue = true;
    public static String
            DISTANCE_BROADCAST = GpsServices.class.getName() + "Location Broadcast",
            EXTRA_SPEED = "extra_speed",
            EXTRA_DISTANCE = "extra_distance";
    Button btnFusedLocation;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    double lat1=0.0;
    double lon1=0.0;
    double lat2 = 0.0;
    double lon2 = 0.0;
    double time=10;
    double calculatedSpeed = 0.0;
    double totalDistance = 0;
    boolean isSlow = false;
    long newTime;
    long oldTime = 0;
    long timeElapsed = 0;
    double doubleTimeElapsed = 0;
    double currentSpeed;
    private double EARTH_RADIUS = 6371000.000;

    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        checkPermission(this);
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            currentSpeed = mCurrentLocation.getSpeed();
            lat2 = mCurrentLocation.getLatitude();
            lon2 = mCurrentLocation.getLongitude();
            newTime = System.currentTimeMillis();
//            System.out.println("Lat2: " + lat2 + "Lon2: " + lon2);
            Log.d("Lat2", String.valueOf(lat2));
            Log.d("Lon2", String.valueOf(lon2));
            if (oldTime != 0) {
                timeElapsed = (newTime - oldTime);
                doubleTimeElapsed = timeElapsed * .001;
            }
            double distance = CalculationByDistance(lat2, lon2, lat1, lon1);
            totalDistance = totalDistance + distance;
            if (timeElapsed != 0) {
                calculatedSpeed = distance / doubleTimeElapsed;
            } else {
                calculatedSpeed = distance / 10;
            }

            NumberFormat formatter = new DecimalFormat("#0.000");
            if (showGPSDialogue == true) {
                Toast.makeText(getApplicationContext(),
                        "Lat is: " + lat2 + "\n Lon is: " + lon2 +
//                                "\n Distance is: " + formatter.format(distance) +
//                                "\n Time Elasped is: " + doubleTimeElapsed +
//                                "\n Total Distance is:" + formatter.format(totalDistance) +
                                "\n CalcSpeed is: " + formatter.format(calculatedSpeed) +
                                "\n getSpeed is: " + formatter.format(currentSpeed)
                        , Toast.LENGTH_LONG).show();
            }
            lat1 = lat2;
            lon1 = lon2;
            oldTime = newTime;

        } else {
            Log.d(TAG, "location is null ...............");
        }

        if (isMyServiceRunning(PretendKiosk.class) == false) {
            //
            final double criticalSpeed = 1;
            if (mCurrentLocation.getSpeed() >= criticalSpeed  && lockIsListening) {
                Log.d(TAG, "Lock is triggering");
                Intent startLock = new Intent(getApplicationContext(), PretendKiosk.class);
                startService(startLock);
            }/* else if (isMyServiceRunning(PretendKiosk.class) == true) {
                    if (mCurrentLocation.getSpeed() <= criticalSpeed || calculatedSpeed <= criticalSpeed) {
                        isSlow = true;
                        Timer timer = new Timer();
                        TimerTask hourlyTask = new TimerTask() {
                            @   Override
                            public void run() {
                                if (mCurrentLocation.getSpeed() >= criticalSpeed ) {
                                    cancel();
                                } else {
                                    Intent stopLock = new Intent(getApplicationContext(), PretendKiosk.class);
                                    stopService(stopLock);
                                }
                            }
                        };
                        timer.schedule(hourlyTask, 01, 1000 * 60 * 60);
                    }
                }*/
            }
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

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    private void sendBroadcastMessage(double distance, double speed) {
        double roundedDistance= Math.round((distance * 100)) / 100.0d;
        double roundedSpeed= Math.round((speed * 100)) / 100.0d;
        Intent intent = new Intent(DISTANCE_BROADCAST);
        intent.putExtra(EXTRA_DISTANCE,roundedDistance);
        intent.putExtra(EXTRA_SPEED,roundedSpeed);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {

        //clean up for app closing
        stopSelf();
        Log.i(TAG, "Stopping service 'GPSServices'");

        super.onDestroy();
    }


}