package com.example.trainningtimer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.sql.Date;

public class ClaseForTime extends Activity {
    Button playStop, setTiempo;
    ImageButton bRestart;
    boolean contando = false, hilo=true, cuentaIniciada=false, finalizado = false;
    SoundPool sp;
    int seg_0;
    TextView tiempo, tv_mins, timeCap;
    Thread crono;
    int seg=0, min=0;
   // String seleccionTiempo;
    Handler h = new Handler();

   EditText et;

    private int limite= (int) Double.POSITIVE_INFINITY;
    private String seleccion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fortime);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



     // Enlace entre objetos y views ademas de eventos

        playStop=findViewById(R.id.ft_PlayStop);
        bRestart =findViewById(R.id.ft_restart);
        setTiempo=findViewById(R.id.ft_b_set);
        tiempo= findViewById(R.id.teview_tiempo);
        timeCap=findViewById(R.id.teView_timeCap);
        tv_mins=findViewById(R.id.ft_tv_mins);
        et=findViewById(R.id.et_fortime);

        sp=new SoundPool(1,AudioManager.STREAM_MUSIC, 1);
        seg_0= sp.load(this,R.raw.cero,1);

        playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cuentaIniciada) {
                    iniciarCuenta();
                }
                actualizarBotones();
                actualizarTexto();



            }
        });
        bRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetContador();


            }
        });
        setTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 seleccion=et.getText().toString();

                if(seleccion.length()==0){
                    Toast.makeText(ClaseForTime.this,"El boton 'set' solo debe ser pulsado si se quiere poner un limite", Toast.LENGTH_LONG).show();
                    return;

                }

                if(limite<=0) {
                    Toast.makeText(ClaseForTime.this,"Es imposible contar en negativo, por favor introduzca un valor positivo", Toast.LENGTH_LONG).show();
                    return;
                }
                limite=Integer.parseInt(seleccion);





                tiempo.setTextSize(60);
                et.setText("");
            }


        });

       /* if(savedInstanceState!= null){
           min= savedInstanceState.getInt("mins");
           seg= savedInstanceState.getInt("segs");
           seleccion= savedInstanceState.getString("seleccion");
           limite= savedInstanceState.getInt("limite");
            contando= savedInstanceState.getBoolean("contando");
            hilo= savedInstanceState.getBoolean("hilo");
            cuentaIniciada= savedInstanceState.getBoolean("cuentaIniciada");
            finalizado=savedInstanceState.getBoolean("finalizado");
            iniciarCuenta();



            actualizarBotones();
            actualizarTexto();



        }*/

        }

        private void iniciarCuenta(){
        cuentaIniciada=true;
            hilo=true;
            crono=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (hilo){
                        if(contando){



                            try{
                                Thread.sleep(1000);


                            }catch (InterruptedException e){
                                e.printStackTrace();

                            }
                            seg++;
                            if(seg==60){
                                min++;
                                seg=0;

                            }

                            if(!finalizado) {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        actualizarTexto();
                                        finalForTime();


                                    }
                                });
                            }



                        }


                    }

                }


            });
            crono.start();

        }


    private void resetContador(){
        min =0;
        seg=0;
        contando=false;
        //hilo= false;
        cuentaIniciada=false;
        actualizarTexto();
        //actualizarBotones();
        //tiempo.setTextSize(60);

    }
    private void actualizarBotones(){

        if(contando){
            et.setVisibility(View.VISIBLE);
            tv_mins.setVisibility(View.VISIBLE);
            timeCap.setVisibility(View.VISIBLE);
            bRestart.setVisibility(View.VISIBLE);
            setTiempo.setVisibility(View.VISIBLE);
            playStop.setText(getString(R.string.b_start_timer));
            contando=false;
            //hilo= false;

        }else{

            et.setVisibility(View.INVISIBLE);
            tv_mins.setVisibility(View.INVISIBLE);
            timeCap.setVisibility(View.INVISIBLE);
            bRestart.setVisibility(View.INVISIBLE);
            setTiempo.setVisibility(View.INVISIBLE);
            playStop.setText(getString(R.string.b_detener_timer));
            contando=true;
            //hilo= true;
        }
        if(limite<0){
            playStop.setVisibility(View.INVISIBLE);

        }else{
            playStop.setVisibility(View.VISIBLE);
        }







    }
    private void actualizarTexto(){

        String m="", s="";
        if(seg<10){
            s="0"+ seg;


        }else{
            s=""+seg;


        }
        if(min<10){
            m="0"+ min;


        }else{
            m=""+min;


        }

        tiempo.setText(m+":"+s);

    }
    public void finalForTime(){

        if(min==limite){
            contando=false;
            hilo=false;
            cuentaIniciada=false;
            finalizado=true;
            actualizarBotones();
            //bRestart.setVisibility(View.VISIBLE);
            tiempo.setTextSize(35);
            tiempo.setText(getString(R.string.tview_emom_final));

            registrarEntreno();
            sp.play(seg_0,1,1,1,0,0);



            Intent intent= new Intent(this, ClaseFinalForTime.class);
            startActivity(intent);
            finish();
        }

    }
    public void registrarEntreno(){
        long ahora = System.currentTimeMillis();
        Date d_fecha = new Date(ahora);
        String nombre=getString(R.string.b1_p_principal);
        String fecha= d_fecha.toString();

        DbHelper dbHelper= new DbHelper(ClaseForTime.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues registro= new ContentValues();
        registro.put(FeedReaderContract.FeedEntry.TIPO_ENTRENO, nombre);
        registro.put(FeedReaderContract.FeedEntry.FECHA, fecha);

        db.insert(FeedReaderContract.FeedEntry.NOMBRE_TABLA, null, registro);

        db.close();
        if(db!=null){
            Toast.makeText(ClaseForTime.this, "ENTRENO REGISTRADO", Toast.LENGTH_SHORT);


        }else{
            Toast.makeText(ClaseForTime.this, "ERROR AL REGISTAR EL ENTRENO", Toast.LENGTH_SHORT);

        }



    }

    protected void onStop(){
        super.onStop();
        SharedPreferences prefs= getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor= prefs.edit();

        editor.putInt("limite", limite);
        editor.putBoolean("contando", contando);
        editor.putBoolean("hilo", hilo);
        prefs.getBoolean("hilo", false);
        editor.apply();


    }

    protected void onStart(){
        super.onStart();

        SharedPreferences prefs= getSharedPreferences("prefs", MODE_PRIVATE);


        contando= prefs.getBoolean("contando", false);
        hilo= prefs.getBoolean("hilo", true);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("mins", min);
        outState.putInt("segs", seg);
        outState.putString("seleccion", seleccion);
        outState.putInt("limite", limite);
        outState.putBoolean("contando", contando);
        outState.putBoolean("hilo", hilo);
        outState.putBoolean("cuentaIniciada", cuentaIniciada);
        outState.putBoolean("finalizado", finalizado);
    }


}




