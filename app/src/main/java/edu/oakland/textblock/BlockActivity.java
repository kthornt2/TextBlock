package edu.oakland.textblock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class BlockActivity extends AppCompatActivity {
    private ImageButton EmergencyCall;
    private ImageButton MailButton;
    private ImageButton CameraButton;
    private ImageButton MapButton;
    private TextView drivingStatsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block);

        CameraButton = (ImageButton) findViewById(R.id.camera);
    }


    public void unlockMyPhone(View view) {

        Log.d("BlockActivity", "THIS SHOULD STOP GPS..........................");
        GpsServices.lockIsListening = false;
        Intent pretendKiosk = new Intent(getApplicationContext(), PretendKiosk.class);
        Log.d("BlockActivit", "AND THIS SHOULD STOP KIOSKSERVICE");
        stopService(pretendKiosk);
        // to unlock the phone only when taking phones, in other word, to keep it from being locked.
        // it needs to be locked as soon as finish taking photo unless automatically unlock mode is open.
        GpsServices.lockIsListening = false;
        // to open a camera
        Intent takePhoto = new Intent(this, TakePhotoActivity.class);
        startActivity(takePhoto);
    }

    public void emergencyUnlock(View view) {
        Intent unlock = new Intent(this, PretendKiosk.class);
        stopService(unlock);
        // to keep it from being locked
        GpsServices.lockIsListening = false;
        // return to first screen
        Button emergencyUnlockButton = (Button) findViewById(R.id.emergency_unlock);
        emergencyUnlockButton.setBackgroundColor(Color.GREEN);
        Intent firstActivity = new Intent(this, FirstActivity.class);
        startActivity(firstActivity);

    }
}
