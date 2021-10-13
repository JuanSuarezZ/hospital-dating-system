package com.example.proyectocomp.Entidades;

import java.io.Serializable;
import java.util.Date;

public class pacientes implements Serializable {
    int id_paciente,edad;
    String nombre,correo,contraseña,nombreu;
    Date fechacreacion,fechaultimacita;


    public pacientes(int id_paciente, String nombre) {
        this.id_paciente = id_paciente;
        this.nombre = nombre;
    }

    public pacientes() {

    }

    public pacientes(int id_paciente, String nombre,  int edad, Date fechacreacion,String correo, Date fechaultimacita, String nombreu,  String contraseña ) {
        this.id_paciente = id_paciente;
        this.edad = edad;
        this.nombreu = nombreu;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.fechacreacion = fechacreacion;
        this.fechaultimacita = fechaultimacita;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombreu() {
        return nombreu;
    }

    public void setNombreu(String nombreu) {
        this.nombreu = nombreu;
    }

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public Date getFechaultimacita() {
        return fechaultimacita;
    }

    public void setFechaultimacita(Date fechaultimacita) {
        this.fechaultimacita = fechaultimacita;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
