package com.example.proyectocomp.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomp.Entidades.pacientes;
import com.example.proyectocomp.R;

import java.util.ArrayList;

public class AdapterEspecPac extends RecyclerView.Adapter<AdapterEspecPac.ViewHolder> implements View.OnClickListener {

    ArrayList<String> model;
    RecyclerView recyclerView;
    private View.OnClickListener listener;

    public AdapterEspecPac( ArrayList<String> model, RecyclerView recyclerView){
        this.model = model;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public void setOnclicklistener(View.OnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_especialidadpac,null,false);
        view.setOnClickListener(this);
        return new AdapterEspecPac.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtEspecialidad.setText(""+model.get(position));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtEspecialidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtEspecialidad = itemView.findViewById(R.id.txtEspecialidad);
        }

    }


}
