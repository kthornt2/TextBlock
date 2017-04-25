package edu.oakland.textblock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.os.Environment.getExternalStorageDirectory;
import static edu.oakland.textblock.R.string.app_name;


public class Notifications extends AppCompatActivity {
    private Button testButton;
    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        listView = (ListView) findViewById(R.id.list);
        getPhotosFromSever(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent photoAcitivty = new Intent(getApplicationContext(), ShowSelfie.class);
                String photo_url = parent.getItemAtPosition(position).toString();
                Log.d("MyApp", photo_url);
                photoAcitivty.putExtra("PHOTO_URL", photo_url);
                startActivity(photoAcitivty);
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

    private File getFileDirectory() {
        File photoURLsDirectory = null;
        // if there is any external storage mounted
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // to create a parent directory for our app in external storage
            File myAppExternalStorage = new File(getExternalStorageDirectory(), getString(app_name));
            if (!myAppExternalStorage.exists()) {
                myAppExternalStorage.mkdir();
            }
            //  to print to debug
            Log.d("MyApp ExternalStorage", myAppExternalStorage.getAbsolutePath());

            // to create a album directory for your photos
            File photoURLs = new File(myAppExternalStorage, "URLs");
            if (!photoURLs.exists()) {
                photoURLs.mkdir();
            }

            // to print to debug
            Log.d("MyApp myAlbum", photoURLs.getAbsolutePath());
            photoURLsDirectory = photoURLs;
        } else {
            // else return the internal storage for the app
            photoURLsDirectory = getFilesDir();
        }
        return photoURLsDirectory;
    }



}

