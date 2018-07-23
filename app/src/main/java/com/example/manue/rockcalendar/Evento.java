package com.example.manue.rockcalendar;

/**
 * Created by crafa on 06/03/2018.
 */

public class Evento {
    private String id;
    private String nombre;
    private String inicio;

    public Evento(String id, String nombre, String inicio) {
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
