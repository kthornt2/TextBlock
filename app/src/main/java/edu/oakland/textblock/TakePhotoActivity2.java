package edu.oakland.textblock;

/**
 * Created by sweettoto on 1/27/17.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;
import static edu.oakland.textblock.R.string.app_name;

// to open a camera with Class Intent
// doesn't work without any exception.

public class TakePhotoActivity2 extends AppCompatActivity {

    private static final String PICTURE_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private ImageView imageView;
    private Bitmap imageBitmap;
    private File photo;
    private int numbersOfPhoto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo2);

        // set the imageView
        imageView = (ImageView) findViewById(R.id.photoView);

        openAnCamera();
    }

    private void openAnCamera() {
        // to create a instance of Intent to open a camera
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // to store the picture
        photo = generatePhotoPath();
        if (photo != null) {
            // to decide to open which camera, front-facing or back-facing camera
//            takePhoto.

            // to pass a parameter which comprises the photo
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

        } else {
            Log.d("MyApp saveFile", "Failed to save the photo for photo is null");
        }
        // start to invoke a existed camera app
        if (takePhoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePhoto, REQUEST_PICTURE_CAPTURE);
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

            // to display the photo for user viewing
            // it is the full size image
//            showPhoto();
//            showPhoto1(image);
//            showPhoto2(image);
//            numbersOfPhoto++;
            if (numbersOfPhoto++ < 1) {
                // then to open camera again to take an opposite direction photo
                openAnCamera();
            }
        } else {
            Log.d("MyAPP cancel to takePic", "User has cancel to take a picture.\n then we should return to the statue activity");
            Intent returnToStatueActivity = new Intent(this, StatusActivity.class);
        }

    }


    private void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    // here we could display the photo on a view
    private void showPhoto() {
        // there is not enough memory to open up more than a couple camera photos
        // to get the size of the imageView
        int targetWitdth = imageView.getWidth();
        int targetHeight = imageView.getHeight();

        //get the size of the image
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        // set the bitmap options to scale the image decode target
        Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), bmOptions);
        int photoWidth = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        int scaleFactor = 1;
        if (targetWitdth > 0 || targetHeight > 0) {
            scaleFactor = Math.min(photoWidth / targetWitdth, photoHeight / targetHeight);
        }

        // to associate the bitmap to the ImageView
        imageView.setImageBitmap(bitmap);
    }

    private void showPhoto1(Intent image) {
        // the second try to display the photo
        // display the thumbnail of the photo
        Bundle extras = image.getExtras();
        Bitmap imageThumbnail = (Bitmap) extras.get("data");
        imageView.setImageBitmap(imageThumbnail);
    }

    private void showPhoto2(Intent image) {
        // the third try to display the photo
        // display the thumbnail of the photo
        if (imageBitmap != null) {
            imageBitmap.recycle();
        }
        InputStream stream = null;
        try {
            stream = getContentResolver().openInputStream(
                    image.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageBitmap = BitmapFactory.decodeStream(stream);
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(imageBitmap);
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

}