package com.example.proyectocomp.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterCitaMedico extends RecyclerView.Adapter<AdapterCitaMedico.ViewHolder> implements View.OnClickListener {

    ArrayList<Cita> model;
    RecyclerView recyclerView;
    private View.OnClickListener listener;
    String tipo = null;

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public AdapterCitaMedico( ArrayList<Cita> model, RecyclerView recyclerView,String tipo){
        this.model = model;
        this.recyclerView = recyclerView;
        this.tipo = tipo;
    }

    public AdapterCitaMedico( ArrayList<Cita> model, RecyclerView recyclerView){
        this.model = model;
        this.recyclerView = recyclerView;
        this.tipo = "";
    }

    public void setLista(ArrayList<Cita> lista){

        ArrayList<Cita> listavacia = new ArrayList<>();
        model = listavacia;
        model = lista;

    }

    public void setOnclicklistener(View.OnClickListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_historiamedic,null,false);
        view.setOnClickListener(this);
        return new AdapterCitaMedico.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(tipo.equals("paciente")){
            holder.nombrePaciente.setText("Doctor: "+model.get(position).getNombreDoc());
        }else{
            holder.nombrePaciente.setText("Paciente: "+model.get(position).getNombrePac());
        }

        holder.descripcion.setText(model.get(position).getDescripcion());
        Date fechaCita = model.get(position).getFechaCita();

        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        String fechaString = dateFormat.format(fechaCita);

        String[] parts2 = fechaString.split(" ");

        String fecha = parts2[0];
        fecha = fecha + " "+parts2[1]+" "+parts2[2]+" "+parts2[3];
        String hora = parts2[4];

        holder.fechacita.setText("Fecha: "+fecha);
        holder.hora.setText("Hora: "+hora);
        holder.especialidad.setText("Especialidad: "+model.get(position).getEspecialidad());////especialidad

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descripcion,nombrePaciente,fechacita,hora,especialidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcion  = itemView.findViewById(R.id.descripcionhm);
            nombrePaciente  = itemView.findViewById(R.id.nombre_paciente);
            fechacita  = itemView.findViewById(R.id.fechaCita);
            hora = itemView.findViewById(R.id.hora);
            especialidad = itemView.findViewById(R.id.Especialidad);
        }


    }



}
