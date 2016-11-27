package com.teamawesome.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.teamawesome.client.R;

/**
 * Created by Sarah on 11/15/16.
 **/

public class typing_game extends AppCompatActivity implements View.OnClickListener{
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
        userInput = (EditText)findViewById(R.id.type_here);

        //alert user that data has been stored
        //eventually this will let the user know if they need to retype
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("We stored your data.");
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

        //on button click, go to results of game (may change to next game)
        startActivity(new Intent(typing_game.this, game_results.class));
    }

    //typing analysis
    //store data

}
