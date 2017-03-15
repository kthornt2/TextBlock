package edu.oakland.textblock;

import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class Notifications extends AppCompatActivity {
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        testButton = (Button) findViewById(R.id.test_button);
        
        testButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //对话框
                Dialog dialog = new AlertDialog.Builder(Notifications.this)
                        .setTitle("Request For Permission")
                        .setMessage("test the dialog")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener(){
                              public void onClick(DialogInterface dialog, int which){}
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){}
                        })
                        .create();
                dialog.show();
            }
        });


    }

}

