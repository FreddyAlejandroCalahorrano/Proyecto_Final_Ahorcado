package com.example.proyecto_final_ahorcado.model;

public class Jugador {
    private String uid;
    private String Nombre;
    private String Puntaje;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() { return Nombre; }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPuntaje() { return Puntaje; }

    public void setPuntaje(String puntaje) { Puntaje = puntaje; }


    @Override
    public String toString() {
        return Nombre + "\n" + Puntaje + " puntos" ;
    }

}
