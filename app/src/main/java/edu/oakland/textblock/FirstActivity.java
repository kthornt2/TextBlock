package edu.oakland.textblock;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class FirstActivity extends AppCompatActivity {
    private ImageButton settingButton;
    private ImageButton guardianButton;

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
                Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


       /* View.OnClickListener mylistener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
            }
        };
        btn1.setOnClickListener(mylistener);
        btn2.setOnClickListener(mylistener);*/
    }


}
