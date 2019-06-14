package com.jongewaard.dev.duckhunt.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jongewaard.dev.duckhunt.R;

public class RankingActivity extends AppCompatActivity {

    TextView score_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        score_final = (TextView) findViewById(R.id.score_final);

        // Cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        score_final.setTypeface(typeface);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //Con esto cargo el fragmento en el activity
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new UserRankingFragment())
                .commit();


    }

}
