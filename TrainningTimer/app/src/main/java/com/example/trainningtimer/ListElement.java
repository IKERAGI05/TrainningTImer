package com.example.trainningtimer;

public class ListElement {
    public int id;
    public String nombre;
    public String fecha;


    public ListElement(int id,String nombre, String fecha) {
        this.id=id;
        this.nombre = nombre;
        this.fecha = fecha;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
