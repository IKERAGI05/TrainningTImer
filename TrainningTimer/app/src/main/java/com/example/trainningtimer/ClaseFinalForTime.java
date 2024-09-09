package com.example.trainningtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ClaseFinalForTime extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fortime_final);


    }

    public void startMenu(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void startForTime(View view) {
        finish();
        Intent intent = new Intent(this, ClaseForTime.class);
        startActivity(intent);

    }
}
