package com.example.manue.rockcalendar;

/**
 * Created by crafa on 19/02/2018.
 */

public class Acontecimiento {
    private String id;
    private String nombre;
    private String inicio;

    public Acontecimiento(String id, String nombre, String inicio) {
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }
}
