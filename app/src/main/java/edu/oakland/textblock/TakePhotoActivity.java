package edu.oakland.textblock;

/**
 * Created by sweettoto on 1/27/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import static android.os.Environment.getExternalStorageDirectory;
import static edu.oakland.textblock.R.string.app_name;

// to open a camera with Class Intent
// doesn't work without any exception.

public class TakePhotoActivity extends AppCompatActivity {

    private static final String PICTURE_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private final String URL_UPLOAD_PHOTO_IN_PHP = "http://52.41.167.226/UploadPhoto3.php";
    private ImageView imageView;
    private Bitmap imageBitmap;
    private File photo;
    private int numbersOfPhoto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block);

        // set the imageView
//        imageView = (ImageView) findViewById(R.id.photoView);

        openAnCamera();
    }

    private void openAnCamera() {
        // to create a instance of Intent to open a camera
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // to store the picture
        photo = generatePhotoPath();

        if (photo != null) {
            // to decide to open which camera, front-facing or back-facing camera

            // to pass a parameter which comprises the photo
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

            // start to invoke a existed camera app
            if (takePhoto.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePhoto, REQUEST_PICTURE_CAPTURE);
            }
        } else {
            Log.d("MyApp saveFile", "Failed to save the photo for photo is null");
            // to make sure it is under monitor.
            GpsServices.lockIsListening = true;
        }


    }

    /**
     * when the subsequent activity invoked by startActivityForResult(takePhoto,REQUEST_PICTURE_CAPTURE) is done, the system will call this method to handle the result.
     * Hence we will invoke some other method to save the file
     *
     * @param requestCode
     * @param resultData
     * @param image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultData, Intent image) {
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultData == RESULT_OK && photo != null) {
            // to add the photo for system gallery
            addPhotoToGallery();

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String IMEI = telephonyManager.getDeviceId();

//          NetworkUtils networkUtils = new NetworkUtils(IMEI);
//          networkUtils.uploadFileAsync(photo);

            // to prompt users
            Toast.makeText(getApplicationContext(), "Photo has been automatically sent.\n now please switch the len and take another", Toast.LENGTH_LONG);

            // to upload photo
            upload(photo);

            // then to open camera again to take another photo in opposite direction
            if (numbersOfPhoto++ < 1) {
                openAnCamera();
            } else {
                Log.d("MyAPP", "User has finished taking pictures.\n then we should return to the block activity");
//                Intent gpsServices = new Intent(getApplicationContext(), GpsServices.class);
//                startService(gpsServices);
                Intent returnToBlockActivity = new Intent(this, BlockActivity.class);
                startActivity(returnToBlockActivity);
                // keep locking the phone as soon as finish taking photo
                GpsServices.lockIsListening = true;
                Toast.makeText(getApplicationContext(), "Your Photos have been automatically sent.\n please wait for your guardian to unlock your phone.", Toast.LENGTH_LONG);
                // to update status of the user on block screen.
                TextView status = (TextView) findViewById(R.id.textView2);
                status.setText("Please wait for unlochk approval.");
                status.setTextColor(Color.RED);



            }
        } else {
            Log.d("MyAPP", "User has cancel to take a picture.\n then we should return to the statue activity");
            // keep locking the phone if user cancel taking photo
            GpsServices.lockIsListening = true;
            Intent returnToStatueActivity = new Intent(this, BlockActivity.class);
        }

    }

    private void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void showPhoto() {
        imageView.setImageURI(Uri.fromFile(photo));
    }

    /**
     * to generate the path of photo for storage
     *
     * @return
     */
    private File generatePhotoPath() {
        // to construct the name for the photo
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.d("MyApp timestamp", timestamp);
        // we don't need the following line because we will add the prefix and suffix on File.createTempFile()
        String photoName = PICTURE_FILE_PREFIX + timestamp + JPEG_FILE_SUFFIX;

        // to get the directory of the photo
        File directory = getPhotoDirectory();
        Log.d("MyApp: photoDir", directory.getAbsolutePath());

        // to create the photo(file)
        File image = null;
        image = new File(directory, photoName);

        //print to debug
        Log.d("MyApp: ImagePath", image.getAbsolutePath());
        return image;
    }

    // to return a File make it more reliable because we could tell whether the path is valid
    private File getPhotoDirectory() {
        File photoDirectory = null;

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
            File myAlbum = new File(myAppExternalStorage, "Photos");
            if (!myAlbum.exists()) {
                myAlbum.mkdir();
            }

            // to print to debug
            Log.d("MyApp myAlbum", myAlbum.getAbsolutePath());
            photoDirectory = myAlbum;
        } else {
            // else return the internal storage for the app
            photoDirectory = getFilesDir();
        }

        return photoDirectory;
    }

    private void upload(final File photo) {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        // instantiate a StringRequest to get photos' links
        StringRequest getPhotosRequest;

        getPhotosRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_PHOTO_IN_PHP, new Response.Listener<String>() {
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
                // converting Bitmap to string
                String photoString = getStringFromPhoto();

                Map<String, String> params = new Hashtable<String, String>();
                // add IMEI into the request
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String IMEI = telephonyManager.getDeviceId();
                params.put("IMEI", IMEI);
                params.put("filename", photo.getName());
                // add photo into the request
                params.put("photo", photoString);
                Log.d("MyApp photo", photoString);
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

    private String getStringFromPhoto() {
        Uri photoUri = Uri.fromFile(photo);
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodeImage;
    }

}