package com.example.trainningtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ClaseFinalEmom extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_emom_final);




    }

    public void startMenu(View view){
        finish();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    public void startEmom(View view){
        finish();
        Intent intent=new Intent(this,ClaseEmom.class);
        startActivity(intent);

    }
}
