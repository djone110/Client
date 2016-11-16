package com.teamawesome.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.teamawesome.client.R;

/**
 * Created by Sarah on 11/15/16.
 **/

public class typing_game extends AppCompatActivity implements View.OnClickListener{
    //declare done button
    Button done;
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
        //on button click, go to results of game (may change to next game)
        startActivity(new Intent(typing_game.this, game_results.class));
    }

    //typing analysis
}
