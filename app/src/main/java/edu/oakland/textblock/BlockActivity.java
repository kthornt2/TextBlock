package edu.oakland.textblock;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
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
//        EmergencyCall  = (ImageButton) findViewById(R.id.emergency_unlock);
//        MailButton     = (ImageButton) findViewById(R.id.mail);
        CameraButton = (ImageButton) findViewById(R.id.camera);
//        MapButton      = (ImageButton) findViewById(R.id.map);
//        drivingStatsTextView = (TextView) findViewById(R.id.drivingStatusTextView);

        EmergencyCall.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(BlockActivity.this, PretendKiosk.class));
                stopService(new Intent(BlockActivity.this, GpsServices.class));
//                ImageButton btn = (ImageButton)findViewById(R.id.emergency_call);
//                btn.setImageResource(R.drawable.unlocked);
                finish();

            }
        });

        MailButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
            }
        });

        MapButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity"));
                startActivity(intent);
            }
        });

        CameraButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
                startActivity(intent);
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double distance = intent.getDoubleExtra(GpsServices.EXTRA_DISTANCE, 0);
                        double speed = intent.getDoubleExtra(GpsServices.EXTRA_SPEED, 0);
                        drivingStatsTextView.setText("Distance: " + distance + " miles" + " " + "Speed: " + speed + " mph");
                    }

                }, new IntentFilter(GpsServices.DISTANCE_BROADCAST)
        );
    }


    public void unlockMyPhone(View view) {
        Intent takePhoto = new Intent(this, TakePhotoActivity.class);
        startActivity(takePhoto);
    }

    public void emergencyUnlock(View view) {
        //// TODO: 4/15/2017
        Intent unlock = new Intent(getApplicationContext(), PretendKiosk.class);
        stopService(unlock);
        finish();
    }
}
