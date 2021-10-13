package com.example.proyectocomp.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.Doctor;
import com.example.proyectocomp.R;

import java.util.ArrayList;

public class AdapterMedicoPac extends RecyclerView.Adapter<AdapterMedicoPac.ViewHolder> implements View.OnClickListener  {

    ArrayList<Doctor> model;
    RecyclerView recyclerView;
    private View.OnClickListener listener;

    public AdapterMedicoPac( ArrayList<Doctor> model, RecyclerView recyclerView){
        this.model = model;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_doctorespac,null,false);
        view.setOnClickListener(this);
        return new AdapterMedicoPac.ViewHolder(view);
    }
    public void setLista( ArrayList<Doctor> lista ){

        ArrayList<Doctor> listavacia = new ArrayList<>();
        model = listavacia;
        model = lista;

    }

    public void setOnclicklistener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtnombredoc.setText("Doctor: "+model.get(position).getNombre());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnombredoc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtnombredoc = itemView.findViewById(R.id.txtnombredoc);
        }


    }
}
