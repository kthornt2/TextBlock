package edu.oakland.textblock;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by sweettoto on 3/15/17.
 */

public class NetworkUtilsTest {


    @Test
    public void testUploadPhoto() {
        String photoPath = "abc.txt";
        File photo = new File(photoPath);
        if (!photo.exists()) {
            // to create a new file and write something if it doesn't exist.
            try {
                photo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileWriter.write("abc");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (photo.isFile()) {
            NetworkUtils networkUtils = new NetworkUtils();
            networkUtils.uploadFileAsync(photo);
        }

    }
}
