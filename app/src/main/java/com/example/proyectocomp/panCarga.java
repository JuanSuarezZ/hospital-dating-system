package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.Doctor;
import com.example.proyectocomp.Entidades.pacientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class panCarga extends AppCompatActivity {

    //medical data
    String userN, pass, anose, correo, direccion, edad, fechaingreso, id, lugargrado, nombre,identificador;
    ArrayList<Cita> listacitas;
    ArrayList<String> especialidades;
    ArrayList<pacientes> listaPacientes;
    ArrayList<Doctor> listaDoctores;
    ArrayList<Doctor> listadoc = new ArrayList<>();
    TextView txt;
    pacientes paciente;
    int cont;

    animacionCarga t1 = new animacionCarga();

    RequestQueue requestQueue;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pan_carga);
        requestQueue = Volley.newRequestQueue(this);
        progressBar = findViewById(R.id.progress_bar);
        txt = findViewById(R.id.txtPancarga);

        listacitas     = new ArrayList<>();
        listaPacientes = new ArrayList<>();
        especialidades = new ArrayList<>();
        listaDoctores   = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            identificador = bundle.getString("identificador");
            userN = bundle.getString("userN");
            pass = bundle.getString("pass");
        }


        if(identificador.equals("reporte2")){
            txt.setVisibility(View.INVISIBLE);
            t1.start();
            startAsyncRep2();

        }else{
            if(identificador.equals("Paciente")){
                t1.start();
                startAsyncTaskPac();
            }else{
                t1.start();
                startAsyncTask();
            }
        }


    }//fin OnCreate():;;;;;;;;;;;;;;;;;;;;;;;;;;



    //hilo de carga
    class animacionCarga extends Thread {

        public void run() {

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(cont);
                cont= cont+10;

                if (cont == 100) {
                    cont = 0;
                }
            }
    }
    public void enviarData() {

        Intent intent = new Intent(this, NavegationDrawerDoc.class);

        intent.putExtra("userN", userN.trim());
        intent.putExtra("pass", pass.trim());
        intent.putExtra("anose", anose.trim());
        intent.putExtra("correo", correo.trim());
        intent.putExtra("direccion", direccion.trim());
        intent.putExtra("edad", edad.trim());
        intent.putExtra("fechaingreso", fechaingreso.trim());
        intent.putExtra("id", id.trim());
        intent.putExtra("lugargrado", lugargrado.trim());
        intent.putExtra("nombre", nombre.trim());
        intent.putExtra("listaCitas",listacitas);
        intent.putExtra("listaPacientes",listaPacientes);

        t1.interrupt();
        startActivity(intent);
        finish();
    }

    //eviar data a otra actividad
    public void enviarData2() {

        Intent intent = new Intent(this, NavegarionDrawerPac.class);

        intent.putExtra("paciente", paciente);
        intent.putExtra("listaespecialidades",especialidades);
        intent.putExtra("listaCitas",listacitas);
        intent.putExtra("listaDoctores",listaDoctores);
        t1.interrupt();
        startActivity(intent);
        finish();
    }

    private void enviarData3() {

        Intent intent = new Intent(this, reportes2.class);
        intent.putExtra("lista", listadoc);
        intent.putExtra("listaDoc", listaDoctores);
        t1.interrupt();
        startActivity(intent);
        finish();

    }



    public void startAsyncTask() {
        ExampleAsyncTask task = new ExampleAsyncTask(this);
        task.execute();
    }

    //hilo
    private static class ExampleAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<panCarga> activityWeakReference;

        ExampleAsyncTask(panCarga activity) {
            activityWeakReference = new WeakReference<panCarga>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {

            System.out.println("llego aca");
            CargaData();
            System.out.println("cargo la data");
            Cargalista();
            System.out.println("cargo la lista");
            CargalistaPacientes();
            System.out.println("cargo la lista de pacientes");


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("eeeeeee");

            return "Bienvenido!";
        }



        public void CargalistaPacientes(){

            final panCarga activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verPacientes";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response3 = future.get();

                JSONArray x = response3.getJSONArray("data");
                JSONArray y = new JSONArray();
                y = x.getJSONArray(0);

                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);
                    activity.listaPacientes.add(new pacientes(arr2.getInt(0),arr2.getString(1)));
                }

            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void CargaData(){

            final panCarga activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verDatosDoc?userN=" + activity.userN;
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response = future.get();

                activity.id = response.getString("id");
                activity.nombre = response.getString("nombre");
                activity.edad = response.getString("edad");
                activity.direccion = response.getString("direccion");
                activity.correo = response.getString("correo");
                activity.anose = response.getString("anose");
                activity.lugargrado = response.getString("lugargrado");
                activity.fechaingreso = response.getString("fechaingreso");

                System.out.println("aaaaaaaaaaa");
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void Cargalista(){

            final panCarga activity = activityWeakReference.get();

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
            String fechacita,estado,especialidad;
            try {
                JSONObject response2 = future2.get();

                JSONArray x = new JSONArray();
                JSONArray y = new JSONArray();
                JSONObject z = new JSONObject();

                x = response2.getJSONArray("Data");
                y = x.getJSONArray(0);

                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);

                    id_cita = arr2.getInt(0);
                    idPaciente = arr2.getInt(1);
                    idMedico = arr2.getInt(2);
                    descripcion = arr2.getString(3);
                    nombrep = arr2.getString(4);
                    nombredoc = arr2.getString(5);
                    fechacita = arr2.getString(6);
                    estado = arr2.getString(7);
                    especialidad = arr2.getString(8);

                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                    Date date = df.parse(fechacita);

                    activity.listacitas.add(new Cita(id_cita,idMedico,idPaciente,descripcion,nombrep,nombredoc,date,estado,especialidad));
                }

                System.out.println("iiiiiiiiii");
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setVisibility(View.INVISIBLE);
            activity.enviarData();
        }

    }




    //segunda async task -------------------------------------------------------------------------
    public void startAsyncTaskPac() {
        asynctask2 task = new asynctask2(this);
        task.execute();
    }

    //hilo
    private static class asynctask2 extends AsyncTask<Integer, Integer, String> {
        private WeakReference<panCarga> activityWeakReference;

        asynctask2(panCarga activity) {
            activityWeakReference = new WeakReference<panCarga>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {

            final panCarga activity = activityWeakReference.get();

            System.out.println("llego aca");
            CargaData();
            System.out.println("cargo la data");
            CargaEspecialidad();
            System.out.println("cargo la lista de especialidad");
            CargahistoriaM();
            System.out.println("cargo la historia medica");
            CargalistaDoctores();
            System.out.println("cargo la lista de doctores");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("eeeeeee");

            return "Bienvenido!";
        }


        public void CargalistaDoctores(){

            final panCarga activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verDoctores";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response3 = future.get();

                JSONArray x = response3.getJSONArray("data");

                JSONArray y = new JSONArray();
                y = x.getJSONArray(0);

                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);


                    activity.listaDoctores.add(new Doctor(arr2.getInt(0),arr2.getString(1)));

                }

            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void CargaData(){

            final panCarga activity = activityWeakReference.get();

            //hago el json
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("nombre", activity.userN);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            String URL = "https://juansuarezz-servidorflask.zeet.app/verDataPac";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response3 = future.get();

                JSONArray x = response3.getJSONArray("data");
                JSONArray y = new JSONArray();
                y = x.getJSONArray(0);



                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);

                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                    Date date = df.parse(arr2.getString(3));

                    DateFormat df2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                    Date date2 = df.parse(arr2.getString(5));


                    pacientes paciente1 =  new pacientes(arr2.getInt(0),arr2.getString(1),arr2.getInt(2),date,arr2.getString(4),date2,arr2.getString(6),arr2.getString(7));
                    activity.paciente = paciente1;
                }
                System.out.println("el paciente= "+activity.paciente);

            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        public void CargaEspecialidad() {

            final panCarga activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verEspecialidades";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, future, future);
            activity.requestQueue.add(request);

            try {

                JSONObject response = future.get();

                JSONArray x = new JSONArray();
                JSONArray y = new JSONArray();
                JSONObject z = new JSONObject();

                x = response.getJSONArray("nombres");
                y = x.getJSONArray(0);

                for (int i = 0; i < x.length(); i++) {
                    JSONArray arr2 = x.optJSONArray(i);

                    activity.especialidades.add(arr2.getString(1));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public void CargahistoriaM(){

            final panCarga activity = activityWeakReference.get();


            //hago el json
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("id_paciente", activity.paciente.getId_paciente());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            String URL2 = "https://juansuarezz-servidorflask.zeet.app/mishistoriasMedicas";
            RequestFuture<JSONObject> future2 = RequestFuture.newFuture();
            JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.POST, URL2, jsonObject, future2, future2);
            activity.requestQueue.add(request2);

            String cod,id,mensaje,nombrep,nombredoc,descripcion;
            int idPaciente,idMedico,id_cita;
            String fechacita,estado,especialidad;
            try {
                JSONObject response2 = future2.get();

                JSONArray x = new JSONArray();
                JSONArray y = new JSONArray();
                JSONObject z = new JSONObject();

                x = response2.getJSONArray("data");
                y = x.getJSONArray(0);

                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);

                    id_cita = arr2.getInt(0);
                    idPaciente = arr2.getInt(1);
                    idMedico = arr2.getInt(2);
                    descripcion = arr2.getString(3);
                    nombrep = arr2.getString(4);
                    nombredoc = arr2.getString(5);
                    fechacita = arr2.getString(6);
                    estado = arr2.getString(7);
                    especialidad = arr2.getString(8);

                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                    Date date = df.parse(fechacita);

                    activity.listacitas.add(new Cita(id_cita,idPaciente,idMedico,descripcion,nombrep,nombredoc,date,estado,especialidad));
                }

                System.out.println("iiiiiiiiii");
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setVisibility(View.INVISIBLE);
            activity.enviarData2();
        }
    }





    //tercer async task -------------------------------------------------------------------------
    public void startAsyncRep2() {
        asynctask3 task = new asynctask3(this);
        task.execute();
    }

    //hilo
    private static class asynctask3 extends AsyncTask<Integer, Integer, String> {
        private WeakReference<panCarga> activityWeakReference;


        asynctask3(panCarga activity) {
            activityWeakReference = new WeakReference<panCarga>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {

            final panCarga activity = activityWeakReference.get();

            CargaData();
            CargalistaDoctores();


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("eeeeeee");

            return "";
        }


        public void CargalistaDoctores(){

            final panCarga activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verDoctores";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response3 = future.get();

                JSONArray x = response3.getJSONArray("data");

                JSONArray y = new JSONArray();
                y = x.getJSONArray(0);

                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);


                    activity.listaDoctores.add(new Doctor(arr2.getInt(0),arr2.getString(1)));

                }

            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        public void CargaData(){

            final panCarga activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verCitasAllDoc";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL,null, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response3 = future.get();

                JSONArray x = new JSONArray();
                JSONArray y = new JSONArray();
                JSONObject z = new JSONObject();

                x = response3.getJSONArray("data");

                for( int i = 0; i<x.length() ; i++ ){
                    JSONArray arr2 = x.optJSONArray(i);

                    activity.listadoc.add(new Doctor(arr2.getInt(0),arr2.getString(1)));
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            panCarga activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.progressBar.setVisibility(View.INVISIBLE);
            activity.enviarData3();
        }




    }



}








