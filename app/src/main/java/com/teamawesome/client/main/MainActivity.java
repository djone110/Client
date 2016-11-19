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
import com.teamawesome.client.comm.CommManager;
import com.teamawesome.client.misc.KeyboardWindow;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendData(View v) {
        CommManager commManager = new CommManager(this);
        try {
            commManager.sendJSON();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send data to the server. Check app logs for more information.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void clearHistory(View v) {
        KeyboardWindow window = new KeyboardWindow(this);
        window.clearData();
    }

    public void testGame(View v){
        startActivity(new Intent(MainActivity.this, com.teamawesome.game.intro_page_game.class));
    }
}
