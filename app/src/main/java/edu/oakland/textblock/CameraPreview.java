package edu.oakland.textblock;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.content.ContentValues.TAG;
import static android.view.SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS;

/**
 * Created by sweettoto on 1/26/17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Camera camera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;

        // to install a SurfaceHolder.Callback so we get notified when the underlying surface is created and destroyed
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        //deprecated setting, but required on Android Version prior to Android 3.0
        surfaceHolder.setType(SURFACE_TYPE_PUSH_BUFFERS);
    }

    private static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        // ignore: tried to stop a non-existent preview
        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        setCameraDisplayOrientation((Activity) getContext(), Camera.CameraInfo.CAMERA_FACING_BACK, camera);
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size bestSize = null;
        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            if (size.width <= width && size.height <= height) {
                if (bestSize == null) {
                    bestSize = size;
                } else {
                    int bestArea = bestSize.width * bestSize.height;
                    int newArea = size.width * size.height;

                    if (newArea > bestArea) {
                        bestSize = size;
                    }
                }
            }
        }

        parameters.setPictureSize(bestSize.width, bestSize.height);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(parameters);


        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Take care of releasing the Camera preview in your activity.
        camera.stopPreview();
        camera.release();
    }
}
