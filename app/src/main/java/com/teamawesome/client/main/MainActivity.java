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
import android.widget.TextView;
import android.widget.Toast;

import com.teamawesome.client.R;
import com.teamawesome.client.comm.CommManager;
import com.teamawesome.client.misc.KeyboardWindow;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UIUpdate uiUpdate = new UIUpdate();
        uiUpdate.run();
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
        startActivity(new Intent(this, com.teamawesome.game.typing_game.class));
    }
    public void startWindvane(View v){
        startService(new Intent(this, com.teamawesome.client.windVane.windVaneLoop.class));
    }
    private class UIUpdate extends Thread {
        @Override
        public void run() {
            TextView rating = (TextView) findViewById(R.id.MarginDisp);
            TextView confidence = (TextView) findViewById(R.id.ConfidenceDisplay);
            double value = 0;
            String line;

            try{
                InputStreamReader inputStreamReader = new InputStreamReader(openFileInput("keyboardConfidence.txt"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while((line = bufferedReader.readLine()) != null){
                    value = Double.parseDouble(line);
                    rating.setText(line);
                }
                inputStreamReader.close();
                bufferedReader.close();
                if (value > 0 && value < 1000){
                    confidence.setText("Not Concerned.");
                }else if (value > 1000 && value < 2000){
                    confidence.setText("Somewhat Concerned.");
                }else{
                    confidence.setText("Very Concerned.");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
