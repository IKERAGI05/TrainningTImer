package com.example.trainningtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ClaseFinalTabata extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tabata_final);


    }

    public void startMenu(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void startTabata(View view) {
        finish();
        Intent intent = new Intent(this, ClaseTabata.class);
        startActivity(intent);

    }
}
