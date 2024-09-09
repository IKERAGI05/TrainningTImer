package com.example.trainningtimer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.util.Locale;

public class ClaseEmom extends Activity {
    Button setTiempo, playStop;
    EditText et;
    ImageButton restart;
    CountDownTimer contar;
    SoundPool sp;
    int seg_3_2_1, seg_0;

    TextView timer, t_rondas, cont_rondas;
    long tiempoInicioMilis=60000, tiempoFinalizarMilis, finalMilis, milisSeleccionados;
    String seleccion="";
    //contadores de rondas
    int rondasSeleccionadas, contadorRondas=0;

    boolean corriendo=false;
    boolean finalizado= false;
    boolean reproducir= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_emom);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        //IDs
        setTiempo= findViewById(R.id.b_set_emom);
        playStop= findViewById(R.id.button_stop_play_emom);
        restart= findViewById(R.id.emom_restart);
        timer= findViewById(R.id.tiempo_emom);
        t_rondas=findViewById(R.id.teView_emom_sets);
        et=findViewById(R.id.et_emom);
        cont_rondas=findViewById(R.id.contador_rondas);

        playStop.setVisibility(View.INVISIBLE);

        sp=new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        seg_3_2_1= sp.load(this,R.raw.tres,1);
        seg_0= sp.load(this,R.raw.cero,1);


        setTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            seleccion=et.getText().toString();

            if(seleccion.length()==0){
                Toast.makeText(ClaseEmom.this,"Selecciona las rondas que quieras", Toast.LENGTH_SHORT).show();
                return;
            }
                rondasSeleccionadas=Integer.valueOf(seleccion);
                if(rondasSeleccionadas<=0) {
                    Toast.makeText(ClaseEmom.this,"Introduzca un valor positivo", Toast.LENGTH_SHORT).show();
                    return;
                }
                playStop.setVisibility(View.VISIBLE);
                et.setText("");

            }
        });

        playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seleccion.length()==0&&!corriendo){
                    Toast.makeText(ClaseEmom.this,"Selecciona las rondas que quieras", Toast.LENGTH_SHORT).show();
                    return;
                }
                cont_rondas.setText( contadorRondas+"/"+rondasSeleccionadas);
                if(corriendo){
                    detenerCuenta();



                }else{
                    iniciarCuenta();

                }

            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetContadorBoton();
            }
        });
    actualizarTexto();

   /* if(savedInstanceState!=null){

        tiempoInicioMilis= savedInstanceState.getLong("iniciomilis");
       tiempoFinalizarMilis= savedInstanceState.getLong("finalizarmilis");
       rondasSeleccionadas= savedInstanceState.getInt("rondasseleccionadas");
       contadorRondas= savedInstanceState.getInt("contadorRondas");
       seleccion= savedInstanceState.getString("seleccion");
       corriendo= savedInstanceState.getBoolean("corriendo");
       finalizado= savedInstanceState.getBoolean("finalizado");
        reproducir= savedInstanceState.getBoolean("reproducir");

        iniciarCuenta();
        actualizarBotones();
        actualizarBotones();


    }*/




    }
    private void iniciarCuenta(){
        if(!finalizado) {
            reproducir=true;
            finalMilis = (int) (System.currentTimeMillis() + tiempoFinalizarMilis);
            contar = new CountDownTimer(tiempoFinalizarMilis, 1000) {
                @Override
                public void onTick(long l) {
                    tiempoFinalizarMilis = (int) l;

                    actualizarTexto();

                   // finalEmom();
                    sumarRonda();


                }

                @Override
                public void onFinish() {
                    cambioRonda();
                    finalEmom();
                }
            }.start();

            corriendo = true;
            actualizarBotones();
        }
    }



    private void detenerCuenta(){
        contar.cancel();
        corriendo=false;
        actualizarBotones();
    }
    private void resetContadorBoton(){
        tiempoFinalizarMilis =tiempoInicioMilis;
      //  iniciarCuenta();
        actualizarTexto();
        actualizarBotones();
        timer.setTextSize(60);
    }
    private void actualizarBotones(){
        if(corriendo){
            et.setVisibility(View.INVISIBLE);
            setTiempo.setVisibility(View.INVISIBLE);
            t_rondas.setVisibility(View.INVISIBLE);
            restart.setVisibility(View.INVISIBLE);
            cont_rondas.setVisibility(View.VISIBLE);
            playStop.setText(getString(R.string.b_detener_timer));


        }else{
            et.setVisibility(View.VISIBLE);
            setTiempo.setVisibility(View.VISIBLE);
            t_rondas.setVisibility(View.VISIBLE);
            timer.setVisibility(View.VISIBLE);
           // restart.setVisibility(View.VISIBLE);
            cont_rondas.setVisibility(View.INVISIBLE);
            playStop.setText(getString(R.string.b_start_timer));

        }
        if(tiempoFinalizarMilis <1000){
            playStop.setVisibility(View.INVISIBLE);



        }else{
            playStop.setVisibility(View.VISIBLE);



        }
        if(tiempoFinalizarMilis <tiempoInicioMilis){

            restart.setVisibility(View.VISIBLE);

        }else{
            restart.setVisibility(View.INVISIBLE);

        }

        if(tiempoFinalizarMilis <tiempoInicioMilis&&rondasSeleccionadas==contadorRondas){

            restart.setVisibility(View.VISIBLE);

        }else{
            restart.setVisibility(View.INVISIBLE);

        }
    }
    private void actualizarTexto(){
        int mins= (int) ((tiempoFinalizarMilis /1000) / 60);
        int segs= (int) ((tiempoFinalizarMilis /1000) % 60);

        String formatoTiempo= String.format(Locale.getDefault(),"%02d:%02d", mins, segs);
        timer.setText(formatoTiempo);
        reproducirSonido(segs);
    }
    private void finalEmom(){
        if(contadorRondas==rondasSeleccionadas){
            finalizado=true;

            detenerCuenta();


            timer.setTextSize(20);
            timer.setText(getString(R.string.tview_emom_final));
            registrarEntreno();

        Intent intent= new Intent(this, ClaseFinalEmom.class);
        startActivity(intent);
            finish();
        }

    }
    private void sumarRonda(){
        if(tiempoFinalizarMilis <1000&&!finalizado){
            contadorRondas++;
            cont_rondas.setText( contadorRondas+"/"+rondasSeleccionadas);
            resetContadorBoton();



        }
    }
    private void cambioRonda(){
        if(!finalizado) {

            tiempoFinalizarMilis = tiempoInicioMilis;
            iniciarCuenta();
        }
    }
    public void registrarEntreno(){
        long ahora = System.currentTimeMillis();
        Date d_fecha = new Date(ahora);
        String nombre=getString(R.string.b3_p_principal);
        String fecha= d_fecha.toString();

        DbHelper dbHelper= new DbHelper(ClaseEmom.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues registro= new ContentValues();
        registro.put(FeedReaderContract.FeedEntry.TIPO_ENTRENO, nombre);
        registro.put(FeedReaderContract.FeedEntry.FECHA, fecha);

        db.insert(FeedReaderContract.FeedEntry.NOMBRE_TABLA, null, registro);

        db.close();
        if(db!=null){
            Toast.makeText(ClaseEmom.this, "ENTRENO REGISTRADO", Toast.LENGTH_SHORT);


        }else{
            Toast.makeText(ClaseEmom.this, "ERROR AL REGISTAR EL ENTRENO", Toast.LENGTH_SHORT);

        }



    }

    private void reproducirSonido(int segs){

        if(segs==3||segs==2||segs==1){

            sp.play(seg_3_2_1,1,1,1,0,0);
            reproducir=true;

        }
        if(segs==0&&reproducir){
            sp.play(seg_0,1,1,1,0,0);
            reproducir=false;

        }



    }
    protected void onStop(){
        super.onStop();
        SharedPreferences prefs= getSharedPreferences("prefsEmom", MODE_PRIVATE);
        SharedPreferences.Editor editor= prefs.edit();
        editor.putLong("comienzoMilisEmom", tiempoInicioMilis);
        editor.putLong("milisFinalesEmom", tiempoFinalizarMilis);
        editor.putBoolean("corriendo", corriendo);
        editor.putLong("Fin", finalMilis );


        editor.apply();

        if(contar!=null){

            contar.cancel();
        }
    }

    protected void onStart(){
        super.onStart();

        SharedPreferences prefs= getSharedPreferences("prefsEmom", MODE_PRIVATE);
        tiempoInicioMilis=  prefs.getLong("comienzoMilisEmom", 60000);
        tiempoFinalizarMilis=  prefs.getLong("milisFinalesEmom", tiempoInicioMilis);
        corriendo= prefs.getBoolean("corriendo", false);

        actualizarTexto();
        actualizarBotones();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("iniciomilis", tiempoInicioMilis);
        outState.putLong("finalizarmilis", tiempoFinalizarMilis);
        outState.putInt("rondasseleccionadas", rondasSeleccionadas);
        outState.putInt("contadorRondas", contadorRondas);
        outState.putString("seleccion", seleccion);
        outState.getBoolean("corriendo", corriendo);
        outState.getBoolean("finalizado", finalizado);
        outState.getBoolean("reproducir", reproducir);



    }
}