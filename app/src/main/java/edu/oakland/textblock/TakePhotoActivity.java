package edu.oakland.textblock;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

// to open a camera with Class Camera
// doesn't work with exception NullPoint of holder
public class TakePhotoActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // this method will receive the data and write it into a file
            File picture = TakePhotoActivity.getOutputMediaFile(1);

        }
    };
    private Camera camera;
    private CameraPreview cameraPreview;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TextBlock");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TextBlock", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);


        if (checkCameraHardware(this)) {
            Log.d("number of camera: ", String.valueOf(Camera.getNumberOfCameras()));

            // to create an instance of Camera
            try {
                camera = Camera.open();
            } catch (Exception e) {
                Log.d("error", "Camera is occupied.");
            }

            // to get the default parameters and set
//            camera.setParameters(camera.getParameters());

            // to ensure correct orientation of preview
//            camera.setPreviewDisplay();

            // Create our Preview view and set it as the content of our activity.
            cameraPreview = new CameraPreview(this, camera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(cameraPreview);
        } else {
            Log.d("tag", "something wrong with open a camera");
        }
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
//        Camera.getNumberOfCameras();
    }

    /**
     * this method responds to click the button_capture
     *
     * @param view
     */
    public void takePhoto(View view) {
        // get data from the camera
        camera.takePicture(null, null, pictureCallback);
    }

}