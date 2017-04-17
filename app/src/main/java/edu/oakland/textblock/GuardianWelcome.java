package edu.oakland.textblock;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;


public class GuardianWelcome extends AppCompatActivity {
    public final static int REQUEST_CODE = -1010101;
    private ImageButton signOutButton;
    private ImageButton NotifiButton;
    private ImageButton settingButton;
    private ImageButton statisButton;


    /*public void checkDrawOverlayPermission() {
        *//** check if we already  have permission to draw over other apps *//*

        if (!Settings.canDrawOverlays(GuardianWelcome.this)) {
            *//** if not construct intent to request permission *//*
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            *//** request permission via start activity for result *//*
            startActivityForResult(intent, REQUEST_CODE);
        }
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        *//** check if received result code
         is equal our requested code for draw permission  *//*
        if (requestCode == REQUEST_CODE) {
            */

    /**
     * if so check once again if we have permission
     *//*
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_welcome);

        NotifiButton  = (ImageButton) findViewById(R.id.notification_button);
        signOutButton = (ImageButton) findViewById(R.id.sign_out_button);
        settingButton = (ImageButton) findViewById(R.id.settings_button);
        statisButton  = (ImageButton) findViewById(R.id.stats_button);
        signOutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, MainActivity.class);
                startActivity(intent);
            }
        });

        NotifiButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPhotosFromSever();
                Intent intent = new Intent(GuardianWelcome.this, Notifications.class);
                startActivity(intent);
            }
        });

        settingButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, edu.oakland.textblock.Settings.class);
                startActivity(intent);
            }
        });

        statisButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, Statistic.class);
                startActivity(intent);
            }
        });


    }

    /*
        public void getPhoto(View view) {
            getPhotosFromSever();
        }
    */
    private void getPhotosFromSever() {
        final String URL_GETPHOTOS = "http://52.41.167.226/GetPhotos.php";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        // instantiate a StringRequest to get photos' links
        StringRequest getPhotosRequest;
        getPhotosRequest = new StringRequest(Request.Method.POST, URL_GETPHOTOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyApp Res", response);
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
                Log.d("MyApp IMEI", "860670027551265");
                return params;
            }
        };
        try {
            Log.d("MyApp", getPhotosRequest.getBody().toString());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        requestQueue.add(getPhotosRequest);

    }

}



