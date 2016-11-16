package com.teamawesome.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.teamawesome.client.R;


public class intro_page_game extends AppCompatActivity implements View.OnClickListener{

    //Declaration Button
    Button YESbutton;
    Button NObutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page_game);

        //intialization button
        YESbutton = (Button) findViewById(R.id.yes_play_game);
        YESbutton.setOnClickListener(intro_page_game.this);
        NObutton = (Button) findViewById(R.id.no_play_game);
        NObutton.setOnClickListener(intro_page_game.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.yes_play_game:
                startActivity(new Intent(intro_page_game.this, typing_game.class));
                break;

            case R.id.no_play_game:
                //startActivity(new Intent(intro_page_game.this, client.class));
                break;

            default:
                break;
        }

    }


}
