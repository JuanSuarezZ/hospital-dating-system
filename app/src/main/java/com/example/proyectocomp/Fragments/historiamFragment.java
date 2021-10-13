package com.example.proyectocomp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.pacientes;
import com.example.proyectocomp.R;
import com.example.proyectocomp.adaptadores.AdapterCita;
import com.example.proyectocomp.adaptadores.AdapterCitaMedico;
import com.example.proyectocomp.adaptadores.AdapterPaciente;
import com.example.proyectocomp.panCarga;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class historiamFragment extends Fragment {

    //variables pacientes
    ArrayList<pacientes> listaPacientes = new ArrayList<>();
    ArrayList<Cita> listaCitas = new ArrayList<>();
    String id_medico;
    ProgressBar progressBar;
    RequestQueue requestQueue;
    RecyclerView recyclerViewhistorias;
    AdapterCitaMedico adapterhistorias;

    //hilo
    historiamFragment.animacionCarga t1 = new historiamFragment.animacionCarga();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.historiam_fragment, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            id_medico = bundle.getString("id", "none");
            listaPacientes = (ArrayList<pacientes>) bundle.getSerializable("listaPacientes");

        }
        //inicializar request
        requestQueue = Volley.newRequestQueue(getContext());


        progressBar = view.findViewById(R.id.progress_bar);

        //recycler 1
        final RecyclerView recyclerView = view.findViewById(R.id.grid_pacientes);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mLinearLayoutManager);

        final AdapterPaciente adapter = new AdapterPaciente(listaPacientes, recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setOnclicklistener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int id_paciente = listaPacientes.get(recyclerView.getChildAdapterPosition(v)).getId_paciente();
                String nombrepaciente = listaPacientes.get(recyclerView.getChildAdapterPosition(v)).getNombre();
                recyclerViewhistorias.setVisibility(View.INVISIBLE);
                ArrayList<Cita> listavacia = new ArrayList<>();
                listaCitas = listavacia;
                t1.run();
                progressBar.setVisibility(View.VISIBLE);
                startAsyncTask(id_paciente, nombrepaciente);
                t1.interrupt();
            }
        });

        //recycler 2
        recyclerViewhistorias = view.findViewById(R.id.grid_registro);
        LinearLayoutManager mLinearLayoutManager2 = new LinearLayoutManager(getContext());
        mLinearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewhistorias.setLayoutManager(mLinearLayoutManager2);
        adapterhistorias = new AdapterCitaMedico(listaCitas, recyclerViewhistorias);
        recyclerViewhistorias.setAdapter(adapterhistorias);


        return view;
    }//finOncreate


    //hilo de carga pendientes
    class animacionCarga extends Thread {

        public void run() {

            int cont = 0;
            progressBar.setProgress(cont);
            cont = cont + 10;

            if (cont == 100) {
                cont = 0;
            }
        }
    }

    public void startAsyncTask(int id_paciente, String nombrepaciente) {
        historiamFragment.historiamedica task = new historiamFragment.historiamedica(this, id_paciente, nombrepaciente);
        task.execute();
    }


    //hilo citasPendientes
    private static class historiamedica extends AsyncTask<Integer, Integer, String> {
        private WeakReference<historiamFragment> activityWeakReference;
        JSONArray citas;
        int id_paciente;
        String nombrepaciente;

        historiamedica(historiamFragment activity, int id_paciente, String nombrepaciente) {
            activityWeakReference = new WeakReference<historiamFragment>(activity);
            this.id_paciente = id_paciente;
            this.nombrepaciente = nombrepaciente;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            historiamFragment activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {

            final historiamFragment activity = activityWeakReference.get();

            System.out.println("llego aca");
            String s = historiasmedicas();
            System.out.println("cargo la data");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return s;
        }

        //funcion
        private String historiasmedicas() {

            final historiamFragment activity = activityWeakReference.get();

            //hago el json
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("id_medico_fk", activity.id_medico);
                jsonObject.put("id_paciente", id_paciente);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            String URL = "https://juansuarezz-servidorflask.zeet.app/historiasMedicas";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response = future.get();


                if(response.getString("mensaje").equals("no existe")){
                    return "no tienes citas con ese paciente";
                }

                JSONArray x = response.getJSONArray("data");
                JSONArray y = new JSONArray();
                y = x.getJSONArray(0);

                for (int i = 0; i < y.length(); i++) {
                    JSONArray arr2 = y.optJSONArray(i);

                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                    Date date = df.parse(arr2.getString(0));

                    activity.listaCitas.add(new Cita(arr2.getString(1), nombrepaciente, date,arr2.getString(2)));
                }

                //System.out.println("aaaaaaaaaaa");

            } catch (InterruptedException | JSONException | ExecutionException | ParseException e) {

            }
            return "citas cargadas!";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            historiamFragment activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            historiamFragment activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null) {
                return;
            }
            Toast.makeText(activity.getActivity(), s, Toast.LENGTH_SHORT).show();
            if(s.equals("no tienes citas con ese paciente")){
                activity.t1.interrupt();
                activity.progressBar.setVisibility(View.INVISIBLE);
            }else{
                activity.progressBar.setVisibility(View.INVISIBLE);
                activity.adapterhistorias.setLista(activity.listaCitas);
                activity.recyclerViewhistorias.smoothScrollToPosition(0);
                activity.t1.interrupt();
                activity.recyclerViewhistorias.setVisibility(View.VISIBLE);
                activity.adapterhistorias.notifyDataSetChanged();
            }
            activity.t1.interrupt();


        }


    }
}



