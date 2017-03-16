package edu.oakland.textblock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by sweettoto on 3/15/17.
 */

public class UploadPhotoAsync extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String part1 = params[0];
        String part2 = params[1];
        File photo;
        photo = new File("/Volumes/Toto/totosweet/Pictures/Others/love.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile("/Volumes/Toto/totosweet/Pictures/Others/love.jpg");
        ByteArrayOutputStream photoByte = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, photoByte);

        return null;
    }
}
