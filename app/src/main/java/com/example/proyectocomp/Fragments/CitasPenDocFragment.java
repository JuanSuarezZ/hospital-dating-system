package com.example.proyectocomp.Fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.R;
import com.example.proyectocomp.adaptadores.AdapterCita;
import com.example.proyectocomp.panCarga;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;


public class CitasPenDocFragment extends Fragment {
    /*
    //iniciamos el socket
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://juansuarezz-servidorflask.zeet.app//");
        } catch (URISyntaxException e) {}
    }
    */
    //variables del medico
    String userN,pass,anose,correo,direccion,edad,fechaingreso,id,lugargrado,mensaje,nombre;
    RequestQueue requestQueue;

    //botones
    FloatingActionButton menu;
    FloatingActionButton floating;

    TextView textcontenedor1,txtdate;
    ImageView calendar,searcher;
    ArrayList<Cita> listacitas = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterCita adapter;
    LinearLayoutManager mLinearLayoutManager;
    Toolbar toolbar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    private ProgressBar progressBar;


    //hilo
    CitasPenDocFragment.animacionCarga t1 = new CitasPenDocFragment.animacionCarga();

    //constructor
    public CitasPenDocFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.citasdocfragment,container,false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userN        = bundle.getString("userN", "none");
            pass         = bundle.getString("pass", "none");
            id           = bundle.getString("id", "none");
            nombre       = bundle.getString("nombre", "none");
            edad         = bundle.getString("edad", "none");
            direccion    = bundle.getString("direccion", "none");
            correo       = bundle.getString("correo", "none");
            anose        = bundle.getString("anose", "none");
            lugargrado   = bundle.getString("lugargrado", "none");
            fechaingreso = bundle.getString("fechaingreso", "none");
            listacitas   = (ArrayList<Cita>) bundle.getSerializable("listaCitas");
        }

        //inicializamos objetos del fragment
        txtdate = (TextView) view.findViewById(R.id.txtdate);
        calendar = (ImageView) view.findViewById(R.id.calendar);
        searcher = (ImageView) view.findViewById(R.id.search);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar2);


        //iniciamos el calendario
        Calendar fecha = Calendar.getInstance();
        final int year = fecha.get(Calendar.YEAR);
        final int month = fecha.get(Calendar.MONTH);
        final int day = fecha.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                txtdate.setText(date);
            }
        },year,month,day);

        //onclick de ver citaspendientes
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.Btnvercitas);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    txtdate.setText("Hoy");
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    startAsyncTask2();
            }
        });


        //onClick de los objetos
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dpd.show();
            }
        });

        //buscador
        searcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechaactual = txtdate.getText().toString();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
                String strDate = sdf.format(c.getTime());

                if(fechaactual.equals("Hoy")){

                    Toast.makeText(getContext(), "Ya estas viendo las citas para hoy", Toast.LENGTH_LONG).show();

                }else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    startAsyncTask(fechaactual);
                }
            }
        });


        //socket
        //mSocket.connect();

        //on escucha clientes
       // mSocket.on("message",onNewMessages);

        //request queue
        requestQueue = Volley.newRequestQueue(getContext());


        recyclerView = view.findViewById(R.id.recycler);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        //lista vacia


        adapter = new AdapterCita(getContext(),listacitas,recyclerView,this);
        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        startAsyncTask2();


        return view;
    }//onCreate-----------------------------------------------------------------------------------

    public void aux(){
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    //hilo de carga pendientes
    class animacionCarga extends Thread {

        public void run() {

            int cont= 0;
            progressBar.setProgress(cont);
            cont= cont+10;

            if (cont == 100) {
                cont = 0;
            }
        }
    }


    //el Sockey escuchador del servidor.
    private Emitter.Listener onNewMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            (getActivity()).runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[1];
                    String mensaje;
                    try {
                        mensaje = data.getString("message").toString();
                        System.out.println("----------------------------------------------------------------------------------------------");
                        System.out.println(mensaje);
                        System.out.println("----------------------------------------------------------------------------------------------");
                        //adapter.additem();
                        //adapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        return;
                    }
                }
            });
        }
    };

    //tareas asincronas
    public void startAsyncTask(String fechaactual) {
            CitasPenDocFragment.ExampleAsyncTask task = new CitasPenDocFragment.ExampleAsyncTask(this,fechaactual,id);
            task.execute();
    }

    public void startAsyncTask2() {
        CitasPenDocFragment.AsynccitasPendientes task = new CitasPenDocFragment.AsynccitasPendientes(this);
        task.execute();
    }


    //hilo citasPendientes
    private static class AsynccitasPendientes extends AsyncTask<Integer, Integer, String> {
        private WeakReference<CitasPenDocFragment> activityWeakReference;
        JSONArray citas;
        String fecha = "citaspendientes";

        AsynccitasPendientes(CitasPenDocFragment activity) {
            activityWeakReference = new WeakReference<CitasPenDocFragment>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CitasPenDocFragment activity = activityWeakReference.get();
            if (activity == null ) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {

            final CitasPenDocFragment activity = activityWeakReference.get();

            System.out.println("llego aca");
            try {
                citas = citasPendientes();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("cargo la data");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Citas cargadas!";
        }

        //funcion
        private JSONArray citasPendientes() throws JSONException {
            CitasPenDocFragment activity = activityWeakReference.get();

            JSONArray x = new JSONArray();
            Calendar fecha = Calendar.getInstance();
            final int year = fecha.get(Calendar.YEAR);
            int month = fecha.get(Calendar.MONTH);
            final int day = fecha.get(Calendar.DAY_OF_MONTH);
            String fechaA = null;
            String aux = null;
            if(month< 10){
                aux = "0"+(month+1);
            }else{
                aux = ""+(month+1);
            }

            fechaA = year+"-"+(aux)+"-"+day;

            String URL2 = "https://juansuarezz-servidorflask.zeet.app/CitasPecDoc?id_Doc=" + activity.id+"&fecha="+fechaA;
            RequestFuture<JSONObject> future2 = RequestFuture.newFuture();
            JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, URL2, null, future2, future2);
            activity.requestQueue.add(request2);

            String cod,id,mensaje,nombrep,nombredoc,descripcion;
            int idPaciente,idMedico,id_cita;
            String fechacita,estado;
            try {
                JSONObject response2 = future2.get();

                x = response2.getJSONArray("Data");

                } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        return x;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            CitasPenDocFragment activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null ) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CitasPenDocFragment activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null ) {
                return;
            }

            activity.progressBar.setVisibility(View.INVISIBLE);
            try {
                if(citas == null){
                    Toast.makeText(activity.getActivity(), "no tienes citas pendientes hoy", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity.getActivity(), s, Toast.LENGTH_SHORT).show();
                    activity.adapter.additem(citas,fecha);
                    activity.t1.interrupt();
                    activity.recyclerView.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            activity.adapter.notifyDataSetChanged();
        }


    }

    //hilo historial citas boton fab
    private static class ExampleAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<CitasPenDocFragment> activityWeakReference;
        String fecha,id;
        JSONArray citas;

        ExampleAsyncTask(CitasPenDocFragment activity,String fecha,String id) {
            activityWeakReference = new WeakReference<CitasPenDocFragment>(activity);
            this.fecha = fecha;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CitasPenDocFragment activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null ) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {

            final CitasPenDocFragment activity = activityWeakReference.get();

            System.out.println("llego aca");
            citas = buscarCitas();
            System.out.println("cargo la data");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Citas cargadas!";
        }

        //funcion
        private JSONArray buscarCitas() {
            CitasPenDocFragment activity = activityWeakReference.get();
            final JSONObject[] obj = {null};
            JSONArray x = new JSONArray();

                //hago el json
                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("id_medico_fk", id);
                    jsonObject.put("fecha",fecha);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String URL2 = "https://juansuarezz-servidorflask.zeet.app/BuscarCitas";
                RequestFuture<JSONObject> future2 = RequestFuture.newFuture();
                JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.POST, URL2, jsonObject, future2, future2);
                activity.requestQueue.add(request2);

                try {
                    JSONObject response2 = future2.get();
                    x = response2.getJSONArray("Data");


                }catch (Exception e){

                }

            return x;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            CitasPenDocFragment activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null ) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CitasPenDocFragment activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null ) {
                return;
            }
            Toast.makeText(activity.getActivity(), s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setVisibility(View.INVISIBLE);
            try {
            activity.adapter.additem(citas,fecha);
            activity.t1.interrupt();
            activity.recyclerView.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            activity.adapter.notifyDataSetChanged();
        }




    }








}
