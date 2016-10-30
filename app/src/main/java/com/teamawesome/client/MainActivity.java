package com.teamawesome.client;

import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    Button button ;
    EditText editText;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

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

        Utils u = new Utils();
        JSONObject j = new JSONObject();
        try {
            j.put("test", "hi");
            u.writeJsonToFile(j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
