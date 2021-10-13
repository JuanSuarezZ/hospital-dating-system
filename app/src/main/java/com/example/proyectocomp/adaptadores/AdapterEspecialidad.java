package com.example.proyectocomp.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomp.Entidades.Especialidades;
import com.example.proyectocomp.R;

import java.util.ArrayList;

public class AdapterEspecialidad extends  RecyclerView.Adapter<AdapterEspecialidad.ViewHolderEsp> {

    ArrayList<Especialidades> lista;
    ArrayList<Especialidades> listaEspecialidades;
    RecyclerView recyclerView;
    Toast toast1;

    public AdapterEspecialidad(ArrayList<Especialidades> lista, RecyclerView recyclerView) {
        this.lista = lista;
        this.recyclerView = recyclerView;
        listaEspecialidades = new ArrayList<Especialidades>();
    }

    @NonNull
    @Override
    public ViewHolderEsp onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_especialidad,null,false);

        toast1 = Toast.makeText(view.getContext(), "Se ha agregado la especialidad", Toast.LENGTH_SHORT);

        return new ViewHolderEsp(view);
    }

    public ArrayList<Especialidades> getlista(){
        return listaEspecialidades;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderEsp holder, final int position) {



        holder.asignardata(lista.get(position).getNombre());

        holder.ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                     listaEspecialidades.add(lista.get(holder.getAdapterPosition()));
                     lista.remove(holder.getAdapterPosition());
                     notifyItemRemoved(holder.getAdapterPosition());
                     toast1.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolderEsp extends RecyclerView.ViewHolder {

        ImageView ico;
        EditText txtes;

        public ViewHolderEsp(@NonNull View itemView) {
            super(itemView);
            ico = itemView.findViewById(R.id.icoCheck);
            txtes = itemView.findViewById(R.id.txtEspecialidad);
        }

        public void asignardata(String s) {
            txtes.setText(s);
        }


    }
}
