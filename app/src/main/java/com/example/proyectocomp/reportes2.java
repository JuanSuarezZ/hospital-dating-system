package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.Doctor;
import com.example.proyectocomp.Entidades.Especialidades;
import com.example.proyectocomp.Entidades.pacientes;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class reportes2 extends AppCompatActivity {

    int cont,mes;
    PieChart pieChart;
    RequestQueue requestQueue;
    ArrayList<String> listameses = new ArrayList<>();
    ArrayList<Doctor> listadoc = new ArrayList<>();
    ArrayList<PieEntry> pieEntreis = new ArrayList<>();
    ArrayList<String>  aux = new ArrayList<>();
    int counter=0;
    Spinner spinner,spinerDoctores;
    Button button;
    ImageView calendar;
    TextView txttitulo,txtaux;
    reportes2.animacionCarga t1 = new reportes2.animacionCarga();
    private ProgressBar progressBar;
    ArrayList<Doctor> listaDoctores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            listaDoctores =  (ArrayList<Doctor>) bundle.getSerializable("listaDoc");
            listadoc = (ArrayList<Doctor>) getIntent().getSerializableExtra("lista");
        }


        listameses.add("Mes");
        listameses.add("enero");
        listameses.add("febrero");
        listameses.add("marzo");
        listameses.add("abril");
        listameses.add("mayo");
        listameses.add("junio");
        listameses.add("julio");
        listameses.add("agosto");
        listameses.add("septiembre");
        listameses.add("octubre");
        listameses.add("noviembre");
        listameses.add("diciembre");

        //ocultar barra
        Objects.requireNonNull(getSupportActionBar()).hide();
        requestQueue = Volley.newRequestQueue(this);
        calendar = findViewById(R.id.calendar);
        spinner =  findViewById(R.id.spinner);
        pieChart = findViewById(R.id.graficopastel);
        progressBar = findViewById(R.id.progress_bar);
        txttitulo = findViewById(R.id.txttitle);
        txtaux = findViewById(R.id.txtnumerocitas);
        button = findViewById(R.id.btnrestart);

        ArrayAdapter<CharSequence> adapterSD2 = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,listameses);
        spinner.setAdapter(adapterSD2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mes = position;
                if(mes == 0){

                }else{
                    txttitulo.setText("Citas para el mes de  "+listameses.get(mes));
                    pieChart.setVisibility(View.INVISIBLE);
                    listadoc = new ArrayList<>();
                    pieEntreis = new ArrayList<>();
                    t1.run();
                    startAsyncTask2(listameses.get(mes));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aux.add("Doctor");
        for(int i =0 ; i< listaDoctores.size() ; i++){

            aux.add(listaDoctores.get(i).getNombre());
        }

        spinerDoctores = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapterSD = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,aux);
        spinerDoctores.setAdapter(adapterSD);

        spinerDoctores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mes = position;
                if(mes == 0){

                }else{
                    txttitulo.setText("Citas del doctor "+listaDoctores.get(mes-1).getNombre());
                    //pieChart.setVisibility(View.INVISIBLE);
                    //listadoc = new ArrayList<>();
                    //pieEntreis = new ArrayList<>();
                    //t1.run();
                    startAsyncTask3(listaDoctores.get(mes-1).getId_medico(),listaDoctores.get(mes-1).getNombre());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //calendario
        Calendar fecha = Calendar.getInstance();
        final int year = fecha.get(Calendar.YEAR);
        final int month = fecha.get(Calendar.MONTH);
        final int day = fecha.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fechax = dayOfMonth+"/"+(month+1)+"/"+year;
                Calendar c = Calendar.getInstance();
                c.set(year, month+1, dayOfMonth);
                int ndia =  c.get(Calendar.DAY_OF_WEEK);

                if( ndia == 1 || ndia == 7 ){

                    Toast.makeText(view.getContext(),"No trabajamos los fines de semana", Toast.LENGTH_SHORT).show();
                }else{
                    String date = dayOfMonth+"/"+(month+1)+"/"+year;
                    Toast.makeText(view.getContext(),date, Toast.LENGTH_SHORT).show();
                    txttitulo.setText("Citas del dia "+fechax);
                    pieChart.setVisibility(View.INVISIBLE);
                    listadoc = new ArrayList<>();
                    pieEntreis = new ArrayList<>();
                    t1.run();
                    startAsyncTask(fechax);
                }

            }
        },year,month,day);

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), panCarga.class);
                intent.putExtra("identificador","reporte2");
                startActivity(intent);
                Animatoo.animateSlideLeft(v.getContext());
            }
        });


        creargraf();
    }


    private void actualizardata() {


        for(int i=0;i<listadoc.size();i++){
            // System.out.println( listadoc.get(i).getId_medico()+ listadoc2.get(i).getNombre());
            pieEntreis.add(new PieEntry((float) listadoc.get(i).getId_medico(), listadoc.get(i).getNombre()));
        }
        if(listadoc.size() == 0 ){
            Toast.makeText(this, "No se concretaron citas en esa temporada", Toast.LENGTH_LONG).show();
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntreis,"");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.notifyDataSetChanged();
        pieChart.setVisibility(View.VISIBLE);
    }


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

    public void creargraf(){

        Description description = new Description();
        description.setText("grafico de pastel");
        pieChart.setDescription(description);

        PieDataSet pieDataSet = new PieDataSet(pieEntreis,"");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);


        for(int i=0;i<listadoc.size();i++){
            System.out.println( listadoc.get(i).getId_medico()+ listadoc.get(i).getNombre());
            pieEntreis.add(new PieEntry((float) listadoc.get(i).getId_medico(),  listadoc.get(i).getNombre()));
            System.out.println( pieEntreis.get(i));
        }

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
    }

    public void startAsyncTask(String fechax) {
        reportes2.ExampleAsyncTask task = new reportes2.ExampleAsyncTask(this,fechax);
        task.execute();
    }

    public void startAsyncTask2(String mesx) {
        reportes2.ExampleAsyncTask2 task = new reportes2.ExampleAsyncTask2(this,mesx);
        task.execute();
    }

    public void startAsyncTask3(int id_doc,String nombre) {
        reportes2.ExampleAsyncTask3 task = new reportes2.ExampleAsyncTask3(this,id_doc,nombre);
        task.execute();
    }

    //hilo1
    private static class ExampleAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<reportes2> activityWeakReference;
        String fechax;

        ExampleAsyncTask(reportes2 activity, String fechax) {
            activityWeakReference = new WeakReference<reportes2>(activity);
            this.fechax = fechax;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {


            System.out.println("llego aca");
            CargaData();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("eeeeeee");

            return "grafica actualizada";
        }


        public void CargaData(){

            final reportes2 activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verCitaDia?fechax=" + fechax;
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response3 = future.get();

                System.out.println("llegoo acaaaas");
                JSONArray x = new JSONArray();
                JSONArray y = new JSONArray();
                JSONArray z = new JSONArray();

                x = response3.getJSONArray("Data");
                y = x.optJSONArray(0);

                System.out.println(y);

                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);
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
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setVisibility(View.INVISIBLE);
            activity.actualizardata();
        }
    }


    //hilo2
    private static class ExampleAsyncTask2 extends AsyncTask<Integer, Integer, String> {
        private WeakReference<reportes2> activityWeakReference;
        String mesx;

        ExampleAsyncTask2(reportes2 activity, String mesx) {
            activityWeakReference = new WeakReference<reportes2>(activity);
            this.mesx = mesx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {


            System.out.println("llego aca");
            CargaData();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("eeeeeee");

            return "grafica actualizada";
        }


        public void CargaData(){

            final reportes2 activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verCitaMensual?fechax=" + mesx;
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response3 = future.get();

                System.out.println("llegoo acaaaas");
                JSONArray x = new JSONArray();
                JSONArray y = new JSONArray();
                JSONArray z = new JSONArray();

                x = response3.getJSONArray("Data");
                y = x.optJSONArray(0);

                System.out.println(y);

                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);
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
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setVisibility(View.INVISIBLE);
            activity.actualizardata();
        }

    }


    //hilo3
    private static class ExampleAsyncTask3 extends AsyncTask<Integer, Integer, String> {
        private WeakReference<reportes2> activityWeakReference;
        int id_doc;
        String nombre;

        ExampleAsyncTask3(reportes2 activity, int id_doc,String nombre) {
            activityWeakReference = new WeakReference<reportes2>(activity);
            this.id_doc = id_doc;
            this.nombre = nombre;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {


            System.out.println("llego aca");
            CargaData();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("eeeeeee");

            return "grafica actualizada";
        }


        public void CargaData(){

            final reportes2 activity = activityWeakReference.get();

            String URL = "https://juansuarezz-servidorflask.zeet.app/verCitaDoc?id=" + id_doc;
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, future, future);
            activity.requestQueue.add(request);
            String numero;
            try {
                JSONObject response3 = future.get();

                numero = response3.getString("Data");

                System.out.println(numero);
                activity.txtaux.setText("el doctor "+nombre+" ha realiazdo "+numero+" citas en total");


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reportes2 activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setVisibility(View.INVISIBLE);
           // activity.actualizardata();
        }

    }

    @Override
    public void onBackPressed() {

        counter++;
        if(counter == 2){

            startActivity(new Intent(this, menuAdmin.class));
            Animatoo.animateSlideDown((this));
            this.finish();
        }
    }


}