package com.teamawesome.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.teamawesome.client.R;

import java.util.Arrays;

/**
 * Created by Sarah on 11/15/16.
 **/

public class typing_game extends AppCompatActivity implements View.OnClickListener {
    Button done; //declare done button
    private EditText userInput; //declare user input variable

    //create done button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set layout
        setContentView(R.layout.typing_game);

        //intialization button
        done = (Button) findViewById(R.id.done_typing_game);
        //create action listener
        done.setOnClickListener(typing_game.this);

    }

    //when done button is clicked
    @Override
    public void onClick(View v) {
        //get text from typing field
        userInput = (EditText) findViewById(R.id.type_here);

        //typing analysis
        //store data
        Boolean test = false;
        String text = userInput.getText().toString();
        String[] ary = text.split(" ");
        String[] copiedText = getApplicationContext().getResources().getString(R.string.typing_speed_text).split(" ");

        test = Arrays.equals(ary, copiedText);

        if (test == false) {
            //alert user that data has been stored
            //eventually this will let the user know if they need to retype
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("The text you entered does not match. Please correct your text to move forward.");
            builder1.setCancelable(true);
            builder1.setNeutralButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        } else {
            //on button click, go to results of game (may change to next game)
            Toast.makeText(typing_game.this, "Success!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}

    /*
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /*
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("typing_game Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    */

