package com.example.proyectocomp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.pacientes;
import com.example.proyectocomp.R;

import java.util.ArrayList;

public class AdapterPaciente extends RecyclerView.Adapter<AdapterPaciente.ViewHolder> implements View.OnClickListener {

    ArrayList<pacientes> model;
    RecyclerView recyclerView;
    private View.OnClickListener listener;

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public void setOnclicklistener(View.OnClickListener listener){
        this.listener = listener;
    }


    public AdapterPaciente( ArrayList<pacientes> model, RecyclerView recyclerView){
        this.model = model;
        this.recyclerView = recyclerView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_paciente,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nombrePaciente.setText("Paciente: "+model.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePaciente;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePaciente  = itemView.findViewById(R.id.nombrePaciente);
        }

    }



}
