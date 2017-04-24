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
    public static TextView speedTextview2;
    public static boolean APPROVAL_STATUS = false;
    private ImageButton EmergencyCall;
    private ImageButton MailButton;
    private ImageButton CameraButton;
    private ImageButton MapButton;
    private TextView drivingStatsTextView;
    private Button emergencyUnlockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block);
/*
        Intent startKisok = new Intent(getApplicationContext(), PretendKiosk.class);
        startService(startKisok);
*/
        speedTextview2 = (TextView) findViewById(R.id.speedTextView2);
        emergencyUnlockButton = (Button) findViewById(R.id.emergency_unlock);
        CameraButton = (ImageButton) findViewById(R.id.camera);
        if (!FirstActivity.isEmergencyMode) {
            emergencyUnlockButton.setBackgroundColor(Color.RED);
        }
        // if it is from takePhoto Activity, then change the tip message
        Intent getIntent = getIntent();
        if (getIntent.getBooleanExtra("isWaitingForApproval", false)) {
            // to update status of the user on block screen.
            TextView status = (TextView) findViewById(R.id.textView2);
            status.setText("Please wait for unlock approval.");
            status.setTextColor(Color.RED);
        }

    }

    public void unlock() {
        Intent returnToFirstActivity = new Intent(getApplicationContext(), FirstActivity.class);
        startActivity(returnToFirstActivity);
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
        // to block GPS information popping up
        GpsServices.showGPSDialogue = false;

        // to open a camera
        Intent takePhoto = new Intent(this, TakePhotoActivity.class);
        startActivity(takePhoto);
    }

    public void emergencyUnlock(View view) {
        Intent unlock = new Intent(this, PretendKiosk.class);
        stopService(unlock);
        // to keep it from being locked
        GpsServices.lockIsListening = false;
        emergencyUnlockButton.setBackgroundColor(Color.GREEN);

        // return to first screen
        Intent firstActivity = new Intent(this, FirstActivity.class);
        FirstActivity.isEmergencyMode = true;
        startActivity(firstActivity);
    }
}
