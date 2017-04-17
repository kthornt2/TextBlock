package edu.oakland.textblock;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class FirstActivity extends AppCompatActivity {
    // for GPS
    public final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0x1;
    private ImageButton settingButton;
    private ImageButton guardianButton;
    private String permission = "ACCESS_FINE_LOCATION";
    private Integer GPS_SETTINGS = 0x7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        settingButton=(ImageButton)findViewById(R.id.settings);
        guardianButton=(ImageButton)findViewById(R.id.guard);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstActivity.this,Settings.class);
                startActivity(intent);
            }
        });

        guardianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                Intent intent = new Intent(FirstActivity.this, BlockActivity.class);
                startActivity(intent);
            }
        });

        askForPermission();

    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(FirstActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(FirstActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(FirstActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                ActivityCompat.requestPermissions(FirstActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
        startService(new Intent(this, GpsServices.class));
    }

}
