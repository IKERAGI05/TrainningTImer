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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Locale;

public class ClaseAmrap extends Activity {
    Button playStop, seleccionarTiempo;
    ImageButton bRestart;
    ProgressBar pb;
    SoundPool sp;
    int sonido_seg_3_2_1, sonido_seg_0;

    EditText et;
    String seleccion;

    TextView timer, duracion, tv_mins;

    CountDownTimer contar;

    private long tiempoInicioMilis, tiempoFinalizarMilis,finalMilis, milisSeleccionados;

    boolean corriendo=false;
    boolean reproducir=false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_amrap);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        et=findViewById(R.id.et_amrap);
        playStop=findViewById(R.id.button_stop_play_amrap);
        timer= findViewById(R.id.teview_tiempo_amrap);
        duracion= findViewById(R.id.textview2_p_amrap);
        bRestart= findViewById(R.id.amrap_restart);
        seleccionarTiempo =findViewById(R.id.amrap_setTiempo);
        tv_mins=findViewById(R.id.amrap_tv_mins);
        pb=findViewById(R.id.progressBar_amrap);

        sp=new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        sonido_seg_3_2_1 = sp.load(this,R.raw.tres,1);
        sonido_seg_0 = sp.load(this, R.raw.cero,1);


        seleccionarTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 seleccion=et.getText().toString();


                if(seleccion.length()==0){

                    Toast.makeText(ClaseAmrap.this,"Selecciona la duracion de tu AMRAP", Toast.LENGTH_SHORT).show();
                    return;
                }
                 milisSeleccionados=Integer.parseInt(seleccion) * 60000;
                if(milisSeleccionados<=0) {
                    Toast.makeText(ClaseAmrap.this,"Es imposible contar en negativo, por favor introduzca un valor positivo", Toast.LENGTH_LONG).show();
                    return;
                }
                setTiempo(milisSeleccionados);
                et.setText("");
            }
        });


        playStop.setOnClickListener(new View.OnClickListener(){

            //Metodo onClick de el boton de comienzo y pausa


            @Override
            public void onClick(View view) {

                if(corriendo){
                    detenerCuenta();


                }else{
                    iniciarCuenta();

                }

            }
        });
        //metodo Onclick del boton de reseteo
        bRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetContador();

            }
        });
    //actualizarTexto();

       /* if(savedInstanceState!=null){
            tiempoInicioMilis= savedInstanceState.getLong("milisInicio");
            tiempoFinalizarMilis= savedInstanceState.getLong("finalizarMilis");
            seleccion= savedInstanceState.getString("seleccion");
            milisSeleccionados= savedInstanceState.getLong("milisSeleccionados");
            corriendo= savedInstanceState.getBoolean("corriendo");

            iniciarCuenta();

            actualizarBotones();
            actualizarTexto();



        }*/



        }


    private void setTiempo(long milis){
        tiempoInicioMilis= (int) milis;
        resetContador();
    }

    private void iniciarCuenta(){

      finalMilis = (int) (System.currentTimeMillis() + tiempoFinalizarMilis);
        contar=new CountDownTimer(tiempoFinalizarMilis, 1000) {
            @Override
            public void onTick(long l) {
                tiempoFinalizarMilis = (int) l;

                actualizarTexto();








            }
            @Override
            public void onFinish() {
              finalAmrap();

            }
        }.start();

        corriendo=true;
        actualizarBotones();

    }

    private void detenerCuenta(){

        contar.cancel();
        corriendo=false;
        actualizarBotones();


    }
    private void  resetContador(){
        tiempoFinalizarMilis =tiempoInicioMilis;
        actualizarTexto();
        actualizarBotones();
        timer.setTextSize(60);

    }

    private void actualizarTexto(){
    int mins= (int) ((tiempoFinalizarMilis /1000) / 60);
    int segs= (int) ((tiempoFinalizarMilis /1000) % 60);

    String formatoTiempo= String.format(Locale.getDefault(),"%02d:%02d", mins, segs);
    timer.setText(formatoTiempo);
    reproducirSonido(segs);

    }

    private void actualizarBotones(){

        if(corriendo){
            et.setVisibility(View.INVISIBLE);
            seleccionarTiempo.setVisibility(View.INVISIBLE);
            duracion.setVisibility(View.INVISIBLE);
            tv_mins.setVisibility(View.INVISIBLE);
            bRestart.setVisibility(View.INVISIBLE);
            playStop.setText(getString(R.string.b_detener_timer));


        }else{
            et.setVisibility(View.VISIBLE);
            seleccionarTiempo.setVisibility(View.VISIBLE);
            duracion.setVisibility(View.VISIBLE);
            tv_mins.setVisibility(View.VISIBLE);
            playStop.setText(getString(R.string.b_start_timer));

        }
        if(tiempoFinalizarMilis <1000){
            playStop.setVisibility(View.INVISIBLE);



        }else{
            playStop.setVisibility(View.VISIBLE);



        }

        if(tiempoFinalizarMilis <tiempoInicioMilis){

            bRestart.setVisibility(View.VISIBLE);

        }else{
            bRestart.setVisibility(View.INVISIBLE);

        }




    }

    public void finalAmrap(){
        corriendo=false;
        actualizarBotones();
        timer.setTextSize(20);
        timer.setText(getString(R.string.tview_emom_final));
        registrarEntreno();
        finish();

        Intent intent= new Intent(this, ClaseFinalAmrap.class);
        startActivity(intent);
    }
    public void registrarEntreno(){
        long ahora = System.currentTimeMillis();
        Date d_fecha = new Date(ahora);
        String nombre=getString(R.string.b2_p_principal);
        String fecha= d_fecha.toString();

        DbHelper dbHelper= new DbHelper(ClaseAmrap.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues registro= new ContentValues();
        registro.put(FeedReaderContract.FeedEntry.TIPO_ENTRENO, nombre);
        registro.put(FeedReaderContract.FeedEntry.FECHA, fecha);

        db.insert(FeedReaderContract.FeedEntry.NOMBRE_TABLA, null, registro);

        db.close();
        if(db!=null){
            Toast.makeText(ClaseAmrap.this, "ENTRENO REGISTRADO", Toast.LENGTH_SHORT);


        }else{
            Toast.makeText(ClaseAmrap.this, "ERROR AL REGISTAR EL ENTRENO", Toast.LENGTH_SHORT);

        }



    }

    private void reproducirSonido(int segs){
     if(segs==3||segs==2||segs==1) {

         sp.play(sonido_seg_3_2_1, 1, 1, 1,0,0);
                 reproducir=true;
     }

     if(segs==0&&reproducir){
         sp.play(sonido_seg_0, 1, 1, 1,0,0);
         reproducir=false;

     }



    }
   protected void onStop(){
    super.onStop();
        SharedPreferences prefs= getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor= prefs.edit();
        editor.putLong("comienzoMilis", tiempoInicioMilis);
        editor.putLong("milisFinales", tiempoFinalizarMilis);
        editor.putBoolean("corriendo", corriendo);
        editor.putLong("Fin", finalMilis );

        editor.apply();

        if(contar!=null){

            contar.cancel();
        }
    }

    protected void onStart(){
        super.onStart();

        SharedPreferences prefs= getSharedPreferences("prefs", MODE_PRIVATE);
        tiempoInicioMilis=  prefs.getLong("comienzoMilis", 600000);
        tiempoFinalizarMilis= prefs.getLong("milisFinales", tiempoInicioMilis);
        corriendo= prefs.getBoolean("corriendo", false);

        actualizarTexto();
        actualizarBotones();

    }

   @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("milisInicio", tiempoInicioMilis);
        outState.putLong("finalizarMilis", tiempoFinalizarMilis);
        outState.putString("seleccion", seleccion);
        outState.putLong("milisSeleccionados", milisSeleccionados);
        outState.putBoolean("corriendo", corriendo);


    }


}


