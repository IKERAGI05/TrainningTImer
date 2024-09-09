package com.example.trainningtimer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    ArrayList<Entrenos> listaEntrenos;



    public ListAdapter(ArrayList<Entrenos> listaEntrenos){

        this.listaEntrenos = listaEntrenos;


    }
    @Override
    public int getItemCount(){
        return listaEntrenos.size();
    }
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, null, false);
        return new ListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder,  int position){
        holder.nombre.setText(listaEntrenos.get(position).getTipo_entreno());
        holder.fecha.setText(listaEntrenos.get(position).getFecha());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iconImage;
        TextView nombre, fecha;
       public ViewHolder(View itemView){
            super(itemView);
            iconImage=itemView.findViewById(R.id.IconImageView);
            nombre= itemView.findViewById(R.id.nombre_tview);
            fecha= itemView.findViewById(R.id.fecha_tview);

        }

    }
}

