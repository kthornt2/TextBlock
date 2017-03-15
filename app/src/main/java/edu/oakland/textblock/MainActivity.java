package edu.oakland.textblock;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.provider.Settings;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;


public class MainActivity extends AppCompatActivity {
    private ImageButton signOutButton;
    private ImageButton NotifiButton;
    private ImageButton settingButton;
    private ImageButton statisButton;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.guardian_welcome);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        NotifiButton  = (ImageButton) findViewById(R.id.notification_button);
        signOutButton = (ImageButton) findViewById(R.id.sign_out_button);
        settingButton = (ImageButton) findViewById(R.id.settings_button);
        statisButton  = (ImageButton) findViewById(R.id.stats_button);
        signOutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


            }



        });

        NotifiButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Notifications.class);
                startActivity(intent);
            }
        });


        settingButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        statisButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(MainActivity.this, Statistic.class);
                //startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        // nothing to do here
        // … really
    }
}



