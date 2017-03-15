package edu.oakland.textblock;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;



public class BlockActivity extends AppCompatActivity {
    private ImageButton EmergencyCall;
    private ImageButton MailButton;
    private ImageButton CameraButton;
    private ImageButton MapButton;
    private KioskContext instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block);


        EmergencyCall  = (ImageButton) findViewById(R.id.emergency_call);
        MailButton     = (ImageButton) findViewById(R.id.mail);
        CameraButton   = (ImageButton) findViewById(R.id.camera);
        MapButton      = (ImageButton) findViewById(R.id.map);

        EmergencyCall.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

            stopService(new Intent(BlockActivity.this, PretendKiosk.class));
                ImageButton btn = (ImageButton)findViewById(R.id.emergency_call);
                btn.setImageResource(R.drawable.unlocked);

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
    }

}
