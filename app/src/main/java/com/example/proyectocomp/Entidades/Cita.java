package com.example.proyectocomp.Entidades;
import java.io.Serializable;
import java.util.Date;


public class Cita implements Serializable {
    int id_cita;
    int id_medico;
    int id_usuario;
    String descripcion;
    String nombrePac;
    String nombreDoc;
    Date fechaCita;
    String estado;
    String especialidad;

    public Cita(String descripcion, String nombrePac, Date fechaCita,String especialidad) {
        this.descripcion = descripcion;
        this.nombrePac = nombrePac;
        this.fechaCita = fechaCita;
        this.fechaCita = fechaCita;
        this.especialidad = especialidad;
    }

    public Cita(int id_cita, int id_medico, int id_usuario, String descripcion, String nombrePac, String nombreDoc, Date fechaCita, String estado, String especialidad) {
        this.id_cita = id_cita;
        this.id_medico = id_medico;
        this.id_usuario = id_usuario;
        this.descripcion = descripcion;
        this.nombrePac = nombrePac;
        this.nombreDoc = nombreDoc;
        this.fechaCita = fechaCita;
        this.estado = estado;
        this.especialidad = especialidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public Cita() {
    }

    public String getNombrePac() {
        return nombrePac;
    }

    public void setNombrePac(String nombrePac) {
        this.nombrePac = nombrePac;
    }

    public String getNombreDoc() {
        return nombreDoc;
    }

    public void setNombreDoc(String nombreDoc) {
        this.nombreDoc = nombreDoc;
    }

    public int getId_medico() {
        return id_medico;
    }

    public void setId_medico(int id_medico) {
        this.id_medico = id_medico;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
