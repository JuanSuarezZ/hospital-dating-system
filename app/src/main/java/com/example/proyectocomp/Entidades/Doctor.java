package com.example.proyectocomp.Entidades;

import java.io.Serializable;

public class Doctor implements Serializable {

    int id_medico;
    String nombre;


    public Doctor(int id_medico, String nombre) {
        this.id_medico = id_medico;
        this.nombre = nombre;
    }

    public int getId_medico() {
        return id_medico;
    }

    public void setId_medico(int id_medico) {
        this.id_medico = id_medico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
