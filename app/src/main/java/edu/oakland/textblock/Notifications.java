package edu.oakland.textblock;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

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


public class Notifications extends AppCompatActivity {
    private Button testButton;
    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        testButton = (Button) findViewById(R.id.test_button);

        testButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //对话框
                Dialog dialog = new AlertDialog.Builder(Notifications.this)
                        .setTitle("Request For Permission")
                        .setMessage("test the dialog")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        listView = (ListView) findViewById(R.id.list);
        getPhotosFromSever(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
            }
        });

    }



    private void getPhotosFromSever(final ListView listView) {
        final String URL_GETPHOTOS = "http://52.41.167.226/GetPhotos.php";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        // instantiate a StringRequest to get photos' links
        StringRequest getPhotosRequest;
        getPhotosRequest = new StringRequest(Request.Method.POST, URL_GETPHOTOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyApp Res", response);
                String[] result = response.split(";");
                int numberOfRow = 0;
                numberOfRow = Integer.valueOf(result[0]);
                if (numberOfRow > 0) {
                    List photoURLs = new ArrayList<String>();

                    listAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview_for_listview, photoURLs);
                    for (int i = 1; i < numberOfRow; i++) {
                        photoURLs.add(result[i]);
                    }
                    listView.setAdapter(listAdapter);

                }
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
        try {
            Log.d("MyApp", getPhotosRequest.getBody().toString());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        requestQueue.add(getPhotosRequest);

    }



}

