package com.example.trainningtimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    int codigo_permiso=200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            //verificarPermisos();

    }


    public void forTime(View view){

        Intent intent=new Intent(this,ClaseForTime.class);
        startActivity(intent);



    }

    public void amrap(View view){
        Intent intent=new Intent(this,ClaseAmrap.class);
        startActivity(intent);


    }
    public void emom(View view){
        Intent intent=new Intent(this,ClaseEmom.class);
        startActivity(intent);


    }
    public void tabata(View view){
        Intent intent=new Intent(this,ClaseTabata.class);
        startActivity(intent);


    }
    public void registro(View view){
        Intent intent=new Intent(this,ClaseRegistro.class);
        startActivity(intent);

    }
    public void salir(View view){

        finish();

    }

   /* @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos(){
       int permisosAltavoz= ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS);
       if(permisosAltavoz==PackageManager.PERMISSION_GRANTED) {


       }else{
           requestPermissions(new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, codigo_permiso);

       }
    }*/


}