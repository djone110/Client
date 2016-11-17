package com.teamawesome.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.teamawesome.client.R;
import com.teamawesome.client.comm.CommManager;


/**
 * Created by googlay on 11/15/16.
 */

public class game_results extends AppCompatActivity implements View.OnClickListener{
    //declare done button
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set layout
        setContentView(R.layout.typing_game);

        //intialization button
        done = (Button) findViewById(R.id.done_results);
        //create action listener
        done.setOnClickListener(game_results.this);

    }

    //when done button is clicked
    @Override
    public void onClick(View v) {
        //on button click, go to results of game (may change to next game)
        startActivity(new Intent(game_results.this, CommManager.class));
    }



}
