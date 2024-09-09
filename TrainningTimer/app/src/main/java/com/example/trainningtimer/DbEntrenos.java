package com.example.trainningtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DbEntrenos extends DbHelper{
    Context context;

    public DbEntrenos(Context context) {
        super(context);
        this.context= context;


    }



    public ArrayList<Entrenos> mostrarEntrenos(){
        DbHelper dbHelper= new DbHelper(context);
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        ArrayList<Entrenos> listaEntrenos= new ArrayList<Entrenos>();
        Entrenos entreno= null;
        Cursor cursorEntrenos= null;
        cursorEntrenos= db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedEntry.NOMBRE_TABLA, null);

        if(cursorEntrenos.moveToFirst()){
            do {
                entreno= new Entrenos();
                entreno.setId(cursorEntrenos.getInt(0));
                entreno.setTipo_entreno(cursorEntrenos.getString(1));
                entreno.setFecha(cursorEntrenos.getString(2));
                listaEntrenos.add(entreno);

            }while (cursorEntrenos.moveToNext());

        }
        cursorEntrenos.close();

        return listaEntrenos;

    }
}
