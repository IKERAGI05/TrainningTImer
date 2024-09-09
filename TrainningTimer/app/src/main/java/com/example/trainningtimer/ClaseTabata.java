package com.example.trainningtimer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.util.Locale;

public class ClaseTabata extends Activity {
    TextView timer, rondas, work, rest, t_contador;

    Button playStop, setTiempo;

    EditText etRondas, etWork, etRest;
    SoundPool sp;

    int seg_3_2_1, seg_0;

    long tiempoFinalizarMilis, tiempoInicioMilisWork, tiempoInicioMilisRest;
    int contRondas=0, nRounds ,nWork, nRest;
    boolean corriendo=false;
    boolean trabajando=true;
    boolean finalizado= false;
    boolean setPressed=false;
    boolean reproducir=false;
    CountDownTimer cronoWork, cronoRest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tabata);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        timer=findViewById(R.id.teview_tiempo_tabata);
        rondas=findViewById(R.id.textView_rondas);
        work=findViewById(R.id.tview_work);
        rest=findViewById(R.id.teview_rest);
        t_contador= findViewById(R.id.amrap_contRondas);

        playStop=findViewById(R.id.button_stop_play_tabata);
        setTiempo=findViewById(R.id.tabata_setTiempo);

        etRondas=findViewById(R.id.et_tabata_rounds);
        etWork=findViewById(R.id.et_tabata_work);
        etRest=findViewById(R.id.et_tabata_rest);

        sp=new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        seg_3_2_1= sp.load(this,R.raw.tres,1);
        seg_0= sp.load(this,R.raw.cero,1);








        playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!setPressed){
                    Toast.makeText(ClaseTabata.this, "Debes pulsar el boton 'set' antes de empezar", Toast.LENGTH_SHORT).show();
                    return;

                }
                t_contador.setText(contRondas + "/" + nRounds);
                if(corriendo){
                    detenerCuenta();


                }else{
                    iniciarCuenta();

                }
            }
        });
        setTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sRounds, sWork, sRest;

                sRounds=etRondas.getText().toString();
                sWork=etWork.getText().toString();
                sRest=etRest.getText().toString();

                setPressed=true;


                /////////////////////////////////

                if(sRounds.length()==0||sWork.length()==0||sRest.length()==0) {

                    Toast.makeText(ClaseTabata.this, "Programa tu TABATA antes de empezar", Toast.LENGTH_SHORT).show();
                    return;
                }
                nRounds= Integer.valueOf(sRounds);
                nWork= Integer.valueOf(sWork)*1000;
                nRest= Integer.valueOf(sRest)*1000;
                if(nRounds<=0||nWork<=0||nRest<=0){
                    Toast.makeText(ClaseTabata.this, "Los valores no pueden ser negativos o nulos", Toast.LENGTH_SHORT).show();
                    return;

                }
                rondas.setVisibility(View.INVISIBLE);
                work.setVisibility(View.INVISIBLE);
                rest.setVisibility(View.INVISIBLE);
                etRest.setVisibility(View.INVISIBLE);
                etWork.setVisibility(View.INVISIBLE);
                etRondas.setVisibility(View.INVISIBLE);

                etRest.setText("");
                etWork.setText("");
                etRondas.setText("");
                ////////////////////////////////////

                setInicio(nWork, nRest);

                actualizarTexto();
            }
        });
actualizarTexto();

      /*  if(savedInstanceState!=null){
            tiempoInicioMilisWork= savedInstanceState.getLong("iniciomiliswork");
            tiempoInicioMilisRest=savedInstanceState.getLong("iniciomilisrest");
            tiempoFinalizarMilis= savedInstanceState.getLong("finalizarmilis");
            nRounds= savedInstanceState.getInt("rondasseleccionadas");
            contRondas= savedInstanceState.getInt("contRondas");
            nRest= savedInstanceState.getInt("rest");
            nWork= savedInstanceState.getInt("work");
            corriendo= savedInstanceState.getBoolean("corriendo");
            finalizado= savedInstanceState.getBoolean("finalizado");
            reproducir= savedInstanceState.getBoolean("reproducir");
            setPressed= savedInstanceState.getBoolean("setpressed");
            trabajando= savedInstanceState.getBoolean("trabajando");

            iniciarCuenta();
            actualizarTexto();
            actualizarBotones();



        }*/




    }
    private void iniciarCuenta(){

        if(trabajando) {
            cronoWork = new CountDownTimer(tiempoFinalizarMilis, 1000) {
                @Override
                public void onTick(long l) {
                    tiempoFinalizarMilis = l;

                    actualizarTexto();

                }

                @Override
                public void onFinish() {
                    sumarRonda();
                    finalTabata();

                }
            }.start();
            corriendo = true;
            //trabajando = true;
            actualizarBotones();
        }
        else{
            cronoRest = new CountDownTimer(tiempoFinalizarMilis, 1000) {
                @Override
                public void onTick(long l) {
                    tiempoFinalizarMilis = l;

                    actualizarTexto();

                }

                @Override
                public void onFinish() {
                    sumarRonda();
                    finalTabata();

                }
            }.start();
            corriendo = true;
            //trabajando = true;
            actualizarBotones();

        }
    }

    private void detenerCuenta(){
    if(trabajando) {
        cronoWork.cancel();
    }else {
        cronoRest.cancel();
    }
    corriendo=false;
    actualizarBotones();

    }
    private void setInicio(long work, long rest){
        tiempoInicioMilisWork=work;
        tiempoInicioMilisRest=rest;
        if(trabajando) {
            tiempoFinalizarMilis = tiempoInicioMilisWork;
        }else{
            tiempoFinalizarMilis=tiempoInicioMilisRest;
        }
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
            rondas.setVisibility(View.INVISIBLE);
            work.setVisibility(View.INVISIBLE);
            rest.setVisibility(View.INVISIBLE);
            setTiempo.setVisibility(View.INVISIBLE);
            etRest.setVisibility(View.INVISIBLE);
            etWork.setVisibility(View.INVISIBLE);
            etRondas.setVisibility(View.INVISIBLE);
            playStop.setText(getString(R.string.b_detener_timer));


        }else{
            rondas.setVisibility(View.VISIBLE);
            work.setVisibility(View.VISIBLE);
            rest.setVisibility(View.VISIBLE);
            setTiempo.setVisibility(View.VISIBLE);
            etRest.setVisibility(View.VISIBLE);
            etWork.setVisibility(View.VISIBLE);
            etRondas.setVisibility(View.VISIBLE);
            playStop.setText(getString(R.string.b_start_timer));

        }


    }
    private void finalTabata(){

        if(contRondas==nRounds){
            finalizado=true;
            cronoWork.cancel();
            cronoRest.cancel();
            corriendo=false;
            trabajando=false;
            registrarEntreno();

            Intent intent=new Intent(this,ClaseFinalTabata.class);
            startActivity(intent);
            finish();
        }


    }
    private void sumarRonda(){

        if(tiempoFinalizarMilis<1000&&trabajando){
            contRondas++;
            t_contador.setText(contRondas + "/" + nRounds);
            trabajando=false;
            setInicio(nWork, nRest);
            iniciarCuenta();

        }
        if(tiempoFinalizarMilis<1000&&!trabajando&&!finalizado){

            trabajando=true;
            setInicio(nWork, nRest);
            iniciarCuenta();
        }
    }

    public void registrarEntreno(){
        long ahora = System.currentTimeMillis();
        Date d_fecha = new Date(ahora);
        String nombre=getString(R.string.b4_p_principal);
        String fecha= d_fecha.toString();

        DbHelper dbHelper= new DbHelper(ClaseTabata.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues registro= new ContentValues();
        registro.put(FeedReaderContract.FeedEntry.TIPO_ENTRENO, nombre);
        registro.put(FeedReaderContract.FeedEntry.FECHA, fecha);

        db.insert(FeedReaderContract.FeedEntry.NOMBRE_TABLA, null, registro);

        db.close();
        if(db!=null){
            Toast.makeText(ClaseTabata.this, "ENTRENO REGISTRADO", Toast.LENGTH_SHORT);


        }else{
            Toast.makeText(ClaseTabata.this, "ERROR AL REGISTAR EL ENTRENO", Toast.LENGTH_SHORT);

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
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("iniciomiliswork", tiempoInicioMilisWork);
        outState.putLong("iniciomilisRest", tiempoInicioMilisRest);
        outState.putLong("finalizarmilis", tiempoFinalizarMilis);
        outState.putInt("rondasseleccionadas", nRounds);
        outState.putInt("contadorRondas", contRondas);
        outState.putInt("rest", nRest);
        outState.putInt("work", nWork);
      //  outState.putString("seleccion", seleccion);
        outState.getBoolean("corriendo", corriendo);
        outState.getBoolean("finalizado", finalizado);
        outState.getBoolean("reproducir", reproducir);
        outState.getBoolean("setpressed", setPressed);
        outState.getBoolean("trabajando", trabajando);






    }


}