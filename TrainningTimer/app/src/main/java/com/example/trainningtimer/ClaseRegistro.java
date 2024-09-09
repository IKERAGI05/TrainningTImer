package com.example.trainningtimer;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClaseRegistro extends Activity {

   RecyclerView listaEntrenos;
   ArrayList<Entrenos> listaArrayEntrenos;

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registro);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        DbEntrenos entrenos= new DbEntrenos(this);

        listaArrayEntrenos = new ArrayList<>();
        ListAdapter adapter= new ListAdapter(entrenos.mostrarEntrenos());
        listaEntrenos = findViewById(R.id.recycler);
        listaEntrenos.setHasFixedSize(true);
        listaEntrenos.setLayoutManager(new LinearLayoutManager(this));
        listaEntrenos.setAdapter(adapter);



    }





}
