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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void analyze(View v) {
        Intent i = new Intent(this, AnalyzeResults.class);
        startActivity(i);
    }

    public void clearHistory(View v) {
        Toast.makeText(this, R.string.notImplementedYet, Toast.LENGTH_SHORT).show();
    }
}
