package com.teamawesome.client.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teamawesome.client.R;


public class MainActivity extends AppCompatActivity {

    Button button ;
    Button jsonButton;
    EditText editText;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            editText = (EditText) findViewById(R.id.editText);
            button = (Button) findViewById(R.id.button);
            jsonButton = (Button) findViewById(R.id.jsonButton);
            int permissionLoc = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
             int permissionUsage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.PACKAGE_USAGE_STATS);

            if (permissionUsage != PackageManager.PERMISSION_GRANTED) {

                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                Toast.makeText(MainActivity.this, "We need Usage permission please. :)", Toast.LENGTH_SHORT).show();
            }

            if (permissionLoc != PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
                Toast.makeText(MainActivity.this, "Location permissions would be gr8!", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(MainActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();


            Toast.makeText(MainActivity.this, "asdfasdf", Toast.LENGTH_SHORT).show();

            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    name = editText.getText().toString();
                    if(name.length() == 0){
                        Toast.makeText(MainActivity.this, "HEY! ENTER YOUR NAME.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, name + " is a great name!", Toast.LENGTH_SHORT).show();
                    }


                    return false;
                }
            });

        jsonButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            Intent i = new Intent();
                return false;
            }
        });


    }




}
