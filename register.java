package com.textblock.kevin.mysql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class register extends AppCompatActivity {
    EditText imei, email, password, firstname, lastname, streetaddress, zip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imei = (EditText)findViewById(R.id.et_imei);
        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        firstname = (EditText)findViewById(R.id.et_firstname);
        lastname = (EditText)findViewById(R.id.et_lastname);
        streetaddress = (EditText)findViewById(R.id.et_streetaddress);
        zip = (EditText)findViewById(R.id.et_zip);
    }

    public void OnReg(View view) {
        String str_imei = imei.getText().toString();
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();
        String str_firstname = firstname.getText().toString();
        String str_lastname = lastname.getText().toString();
        String str_streetaddress = streetaddress.getText().toString();
        String str_zip = zip.getText().toString();
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_imei, str_email, str_password, str_firstname, str_lastname, str_streetaddress, str_zip);

    }
}
