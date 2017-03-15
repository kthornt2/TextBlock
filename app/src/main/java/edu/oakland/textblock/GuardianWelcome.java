package edu.oakland.textblock;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.provider.Settings;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;


public class GuardianWelcome extends AppCompatActivity {
    private ImageButton signOutButton;
    private ImageButton NotifiButton;
    private ImageButton settingButton;
    private ImageButton statisButton;

    public final static int REQUEST_CODE = -1010101;


    /*public void checkDrawOverlayPermission() {
        *//** check if we already  have permission to draw over other apps *//*

        if (!Settings.canDrawOverlays(GuardianWelcome.this)) {
            *//** if not construct intent to request permission *//*
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            *//** request permission via start activity for result *//*
            startActivityForResult(intent, REQUEST_CODE);
        }
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        *//** check if received result code
         is equal our requested code for draw permission  *//*
        if (requestCode == REQUEST_CODE) {
            *//** if so check once again if we have permission *//*
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_welcome);

        NotifiButton  = (ImageButton) findViewById(R.id.notification_button);
        signOutButton = (ImageButton) findViewById(R.id.sign_out_button);
        settingButton = (ImageButton) findViewById(R.id.settings_button);
        statisButton  = (ImageButton) findViewById(R.id.stats_button);
        signOutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, MainActivity.class);
                startActivity(intent);
            }
        });

        NotifiButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, Notifications.class);
                startActivity(intent);
            }
        });

        settingButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, Settings.class);
                startActivity(intent);
            }
        });

        statisButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, Statistic.class);
                startActivity(intent);
            }
        });


        }

    }



