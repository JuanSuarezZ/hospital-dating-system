package com.example.proyectocomp.FragmentsPac;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.Doctor;
import com.example.proyectocomp.Entidades.pacientes;
import com.example.proyectocomp.R;
import com.example.proyectocomp.adaptadores.AdapterCita;
import com.example.proyectocomp.adaptadores.AdapterEspecPac;
import com.example.proyectocomp.adaptadores.AdapterMedicoPac;
import com.example.proyectocomp.loginDoc;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PedirCita extends Fragment {

    //recicler1
    RecyclerView recyclerView1;
    LinearLayoutManager mLinearLayoutManager;

    //recicler2
    RecyclerView recyclerView2;
    LinearLayoutManager mLinearLayoutManager2;
    AdapterMedicoPac adapterEspecPac2;

    // pacientes paciente;
    pacientes paciente;
    ArrayList<Cita> listacitas;
    ArrayList<Doctor> listaDoctores;
    ArrayList<String>  listaEspecialidad;

    //layout
    TextView txt1,txt2;
    ImageView hora,fecha;
    Button btnCrear;

    //variables cita
    String Ndoctor = null,fechacita = null,horacita = null,especialidad = null;
    int id_medico_fk = 0;



    private ProgressBar progressBar;
    RequestQueue requestQueue;
    PedirCita.animacionCarga t1 = new PedirCita.animacionCarga();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pedircitapac, container, false);

        //intent
        listacitas = new ArrayList<>();
        listaEspecialidad = new ArrayList<>();
        listaDoctores = new ArrayList<>();
        //request
        requestQueue = Volley.newRequestQueue(getContext());
        Bundle bundle = this.getArguments();

        if (bundle != null) {

            listacitas   = (ArrayList<Cita>) bundle.getSerializable("listaCitas");

            listaEspecialidad   = (ArrayList<String>) bundle.getSerializable("listaEspecialidad");
            paciente  = (pacientes) bundle.getSerializable("paciente");

        }

        //
        txt1 = view.findViewById(R.id.textView15);
        txt2 = view.findViewById(R.id.txt17);
        hora = view.findViewById(R.id.viewhora);
        fecha = view.findViewById(R.id.viewfecha);
        btnCrear = view.findViewById(R.id.botonEnviarcita);

        //button
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Ndoctor == null || fechacita == null || horacita == null || especialidad == null){
                    Toast.makeText(getContext(),"debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                }else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Crear cita");
                    builder.setMessage("Especialidad: "+especialidad +"\n"+
                                        "Doctor: "+ Ndoctor +"\n"+
                                        "Fecha: "+fechacita+"\n"+
                                        "Hora:"+horacita);
                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String myResponse = AñadirCita();

                            System.out.println("resultado= "+myResponse);

                            String Ndoctor = null;
                            fechacita = null;
                            horacita = null;
                            especialidad = null;
                            int id_medico_fk = 0;
                        }
                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar2);


        //request queue
        requestQueue = Volley.newRequestQueue(getContext());

        //recycler1
        recyclerView1 = view.findViewById(R.id.recyclerEspecialidad);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(mLinearLayoutManager);
        AdapterEspecPac adapterEspecPac = new AdapterEspecPac(listaEspecialidad, recyclerView1);
        recyclerView1.setAdapter(adapterEspecPac);

        adapterEspecPac.setOnclicklistener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.run();
                listaDoctores.clear();
                progressBar.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.INVISIBLE);
                recyclerView1.smoothScrollToPosition(0);
                especialidad = listaEspecialidad.get(recyclerView1.getChildAdapterPosition(v));
                startAsyncTask(listaEspecialidad.get(recyclerView1.getChildAdapterPosition(v)));
                //filtra por nombre de las especialidades
            }
        });

        //recycler2 este es el de los doctoreeees
        recyclerView2 = view.findViewById(R.id.recyclerMedicos);
        LinearLayoutManager mLinearLayoutManager2 = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(mLinearLayoutManager2.VERTICAL);
        recyclerView2.setLayoutManager(mLinearLayoutManager2);
        adapterEspecPac2 = new AdapterMedicoPac(listaDoctores, recyclerView2);
        recyclerView2.setAdapter(adapterEspecPac2);

        adapterEspecPac2.setOnclicklistener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id_medico_fk = listaDoctores.get(recyclerView1.getChildAdapterPosition(v)).getId_medico();
                String a = listaDoctores.get(recyclerView1.getChildAdapterPosition(v)).getNombre();
                String b = "el doctor: "+a+" fue seleccionado";
                Toast.makeText(getContext(),b, Toast.LENGTH_SHORT).show();
                Ndoctor = a;

            }
        });

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //iniciamos el calendario
                Calendar fecha = Calendar.getInstance();
                final int year = fecha.get(Calendar.YEAR);
                final int month = fecha.get(Calendar.MONTH);
                final int day = fecha.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar csistema = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        c.set(year, month+1, dayOfMonth);
                        int ndia =  c.get(Calendar.DAY_OF_WEEK);

                        String date;
                        date = dayOfMonth+"/"+(month+1)+"/"+year; //fecha seleccionada

                        String fechasis;
                        fechasis = csistema.get(Calendar.DAY_OF_MONTH)+"/"+(csistema.get(Calendar.MONTH)+1)+"/"+csistema.get(Calendar.YEAR);


                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                        LocalDate fecha1 = LocalDate.parse(date, formatter);
                        LocalDate fecha2 = LocalDate.parse(fechasis, formatter);

                        System.out.println("fecha sel: "+fecha1);
                        System.out.println("fecha sis: "+fecha2);

                        Date date1 = Date.from(fecha1.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        Date date2 = Date.from(fecha2.atStartOfDay(ZoneId.systemDefault()).toInstant());

                        System.out.println("fecha sel: "+date1);
                        System.out.println("fecha sis: "+date2);

                        if(date1.before(date2)){
                            Toast.makeText(getContext(),"No puedes agendar dias anteriores", Toast.LENGTH_SHORT).show();
                        }else{
                            if( ndia == 1 || ndia == 7 ){

                                Toast.makeText(getContext(),"No trabajamos los fines de semana", Toast.LENGTH_SHORT).show();
                            }else{

                                Toast.makeText(getContext(),date, Toast.LENGTH_SHORT).show();
                                fechacita = date;
                            }
                        }

                    }
                },year,month,day);
                dpd.show();



            }
        });

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar fecha = Calendar.getInstance();
                final int hour = fecha.get(Calendar.HOUR_OF_DAY);
                final int minute = fecha.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String date = hourOfDay+":"+minute+":00";
                        Toast.makeText(getContext(),date, Toast.LENGTH_SHORT).show();
                        horacita = date;
                    }
                },hour,minute,false);

                timePickerDialog.show();
            }
        });





        return view;
    }//fin ------------------------------------------------

    public String AñadirCita(){


        final String[] myResponse = {null};
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

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id_paciente_fk",paciente.getId_paciente());
            jsonObject.put("id_medico_fk"  ,id_medico_fk);
            jsonObject.put("nombrepac"     ,paciente.getNombre());
            jsonObject.put("nombredoc"     ,Ndoctor);
            jsonObject.put("fecha"     ,fechaA + " " + horacita);
            jsonObject.put("especialidad"     ,especialidad);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        //aca hago el json
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        okhttp3.Request request = new okhttp3.Request.Builder().url("https://juansuarezz-servidorflask.zeet.app/AgregarCita").post(body).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                   getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(myResponse.equals("El doctor no puede agendar mas citas")){
                                Toast.makeText(getContext(),"El doctor no puede agendar mas citas hoy", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getContext(),"Cita agendada", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });

        System.out.println("respuestaaaa= "+myResponse[0]);
        return myResponse[0];
    }


    public void startAsyncTask(String nombreEspecialidad) {
        PedirCita.verdoc task = new PedirCita.verdoc(this, nombreEspecialidad);
        task.execute();
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

    private static class verdoc extends AsyncTask<Integer, Integer, String> {
        private WeakReference<PedirCita> activityWeakReference;
        String nombreEspecialidad;

        verdoc(PedirCita activity, String nombreEspecialidad) {
            activityWeakReference = new WeakReference<PedirCita>(activity);
            this.nombreEspecialidad = nombreEspecialidad;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PedirCita activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {

            final PedirCita activity = activityWeakReference.get();

            System.out.println("llego aca");
            vermedicosfiltro();
            System.out.println("cargo la data");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "se cargaron los doctores";
        }

        //funcion
        private String vermedicosfiltro() {

            final PedirCita activity = activityWeakReference.get();

            //hago el json
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("especialidad", nombreEspecialidad );

            } catch (JSONException e) {
                e.printStackTrace();
                e.printStackTrace();
            }
            System.out.println("con especialidad");

            String URL = "https://juansuarezz-servidorflask.zeet.app/verDocEspecialidad";
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, future, future);
            activity.requestQueue.add(request);

            try {
                JSONObject response = future.get();


                JSONArray x = response.getJSONArray("data");
                JSONArray y = new JSONArray();
                y = x.getJSONArray(0);


                for( int i = 0; i<y.length() ; i++ ){
                    JSONArray arr2 = y.optJSONArray(i);
                    System.out.println("doctores: "+y);
                    activity.listaDoctores.add(new Doctor(arr2.getInt(0),arr2.getString(1)));



                }

            } catch (InterruptedException | ExecutionException | JSONException e) {

            }
            return "citas cargadas!";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            PedirCita activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PedirCita activity = activityWeakReference.get();
            //|| activity.isFinishing()
            if (activity == null) {
                return;
            }
            Toast.makeText(activity.getActivity(), s, Toast.LENGTH_SHORT).show();

                activity.progressBar.setVisibility(View.INVISIBLE);
                activity.adapterEspecPac2.setLista(activity.listaDoctores);
                activity.t1.interrupt();
                activity.recyclerView2.setVisibility(View.VISIBLE);
                activity.adapterEspecPac2.notifyDataSetChanged();

        }


    }













}
