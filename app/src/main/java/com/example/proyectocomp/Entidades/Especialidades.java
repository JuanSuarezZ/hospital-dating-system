package com.example.proyectocomp.Entidades;

import java.io.Serializable;

public class Especialidades implements Serializable {

    int id;
    String nombre;

    public Especialidades(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
