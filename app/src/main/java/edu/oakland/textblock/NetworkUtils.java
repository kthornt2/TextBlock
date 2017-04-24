package edu.oakland.textblock;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by sweettoto on 2/21/17.
 */

public class NetworkUtils {
    // this URL for uploading photos
    private static final String URL_UPLOAD_PHOTO_IN_PHP = "http://52.41.167.226/UploadPhoto3.php";
    private static int responseCode = 0;
    private static String responseMessage;
    private static String imei;

    public NetworkUtils(String imei) {
        this.imei = imei;
    }

    public void uploadFileAsync(File photo) {
        UploadFileAsync uploadFileAsync = new UploadFileAsync();
        uploadFileAsync.execute(photo);
    }

    private class UploadFileAsync extends AsyncTask<File, Integer, Long> {
        @Override
        protected Long doInBackground(File... params) {
            //final String URL_UPLOAD_PHOTO_IN_PHP = "http://52.41.167.226/UploadPhoto3.php";
            int responseCode = 0;
            String responseMessage;
            File photo = params[0];

            URL destinationURL = null;
            HttpURLConnection httpURLConnection = null;
            DataOutputStream dataOutputStream = null;
            // the following string is used to construct the header.
            String boundary = "--------boundary";
            String delimiter = "--";
            String lineEnd = "\r\n";
            int byteRead, byteAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024 * 3;//3Mb

            try {
                destinationURL = new URL(URL_UPLOAD_PHOTO_IN_PHP);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (destinationURL != null) {
                try {
                    httpURLConnection = (HttpURLConnection) destinationURL.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                // to set up the HTTP request
                // here is the header
                try {
                    httpURLConnection.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                httpURLConnection.setRequestProperty("Connection", "keep-alive");
                httpURLConnection.setRequestProperty("Encryption", "multipart/form-data");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                OutputStream outputStream = null;
                try {
                    outputStream = httpURLConnection.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataOutputStream = new DataOutputStream(outputStream);
                try {
                    // this is the end of the header
                    dataOutputStream.writeBytes(delimiter + boundary + lineEnd);
                    // here is the body of the request
                    {
                        // our first form data: fileName
                        dataOutputStream.writeBytes("Content-disposition: form-data; name='filename'" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(photo.getName() + lineEnd);
                        dataOutputStream.writeBytes(delimiter + boundary + lineEnd);

                        // our second form data: IMEI
                        dataOutputStream.writeBytes("Content-disposition: form-data; name='IMEI'" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(imei + lineEnd);
                        dataOutputStream.writeBytes(delimiter + boundary + lineEnd);

                        {
                            // our second form-data: photo
                            dataOutputStream.writeBytes("Content-disposition: form-data; name='photo'; filename=\'" + photo.getName() + "\'" + lineEnd);
                            dataOutputStream.writeBytes(lineEnd);
                            // to prepare to read from the image file and write into the body of the request
                            FileInputStream fileInputStream = new FileInputStream(photo);
                            // to initialize the byteRead in order to start looping
                            {
                                byteAvailable = fileInputStream.available();
                                bufferSize = Math.min(byteAvailable, maxBufferSize);
                                buffer = new byte[bufferSize];
                                // to start to read from the image file then write into the body of the request
                                byteRead = fileInputStream.read(buffer, 0, bufferSize);// to read bufferSize bytes from the file at one time, byteRead is the numbers of bytes read from the file at the time, and particularly it will be -1 when reading after the end have been reached.

                                while (byteRead > 0) {
                                    // if byteRead >0, write the bytes into the request
                                    dataOutputStream.write(buffer, 0, byteRead);

                                    //then start to loop reading and writing
                                    byteAvailable = fileInputStream.available();
                                    bufferSize = Math.min(byteAvailable, maxBufferSize);
                                    buffer = new byte[bufferSize];
                                    byteRead = fileInputStream.read(buffer, 0, bufferSize);
                                }
                                dataOutputStream.writeBytes(lineEnd);
                                dataOutputStream.writeBytes(delimiter + boundary + delimiter);
                            }
                            dataOutputStream.flush();
                            dataOutputStream.close();
                            fileInputStream.close();
                            outputStream.close();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    httpURLConnection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // to get the response code
                try {
                    responseCode = httpURLConnection.getResponseCode();
                    System.out.println("responseCode: " + responseCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //to get the response message
                try {
                    responseMessage = httpURLConnection.getResponseMessage();
                    System.out.println("responseMessage: " + responseMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (responseCode == 200) {
                    String response = getResponse(httpURLConnection);
                    System.out.println(response);

                    if (response.equals("1;") || response == "1;") {
                        // it means the users can be unlocked once they upload their photos
//                        UnlockAssistant.stopListening();
                        BlockActivity.APPROVAL_STATUS = true;
                        Log.d("MyApp", "Phone is permitted to be unlocked.");
                    }
                }
                httpURLConnection.disconnect();
            }
            return null;
        }

        private String getResponse(HttpURLConnection connection) {
            String result = null;
            StringBuffer stringBuffer = new StringBuffer();
            InputStream inputStream = null;

            try {
                inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine = "";
                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
                result = stringBuffer.toString();
            } catch (Exception e) {
                result = null;
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPostExecute(Long result) {

        }
    }
}
