package edu.oakland.textblock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Rem on 4/24/2017.
 */

public class ShowSelfie extends Activity {
    private String photo_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_selfie);
        setCurrentImage();
        photo_url = getIntent().getStringExtra(photo_url);

    }

    private void setCurrentImage() {
        final ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
        Picasso.with(getApplicationContext()).load(photo_url).into(imageView);
    }


    public void approve(View view) {
        approveRequest();
        goBack();
    }


    private void goBack() {
        Intent goBackToList = new Intent(getApplicationContext(), Notifications.class);
        startActivity(goBackToList);
    }

    public void refuse(View view) {
        refuseRequest();
        goBack();
    }

    private void refuseRequest() {
        sendRequest(photo_url, "-1");
    }

    private void approveRequest() {
        sendRequest(photo_url, "1");
    }

    private void sendRequest(final String url, final String isApproved) {
        final String URL_GETPHOTOS = "http://52.41.167.226/Authorization.php";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

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
                params.put("IMEI", IMEI);
                params.put("PHOTO_URL", url);
                params.put("ISAPPROVED", isApproved);
                return params;
            }
        };
        requestQueue.add(getPhotosRequest);
    }
}


