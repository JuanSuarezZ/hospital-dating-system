package com.example.proyectocomp.adaptadores;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Fragments.CitasPenDocFragment;
import com.example.proyectocomp.R;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdapterCita extends RecyclerView.Adapter<AdapterCita.ViewHolder> implements View.OnClickListener {

    LayoutInflater inflater;
    ArrayList<Cita> model;
    RecyclerView recyclerView;
    CitasPenDocFragment p1 = new CitasPenDocFragment();
    String fecha;
    private WeakReference<CitasPenDocFragment> activityWeakReference;

    //iniciamos el socket
    /*
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://juansuarezz-servidorflask.zeet.app/");
        } catch (URISyntaxException e) {}
    }
    */

    //listener
    private View.OnClickListener listener;

    public AdapterCita(Context context, ArrayList<Cita> model,RecyclerView recyclerView,CitasPenDocFragment activity){
        activityWeakReference = new WeakReference<CitasPenDocFragment>(activity);
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.recyclerView = recyclerView;
        this.fecha = "hoy";
    }

    public void additem(JSONArray citas,String fecha) throws JSONException, ParseException {

        this.fecha = fecha;

        ArrayList<Cita> listavacia = new ArrayList<>();
        model = listavacia;

        System.out.println("lo de llega "+ citas );

        JSONArray y = new JSONArray();
        y = citas.getJSONArray(0);

        for( int i = 0; i < y.length() ; i++ ){

            JSONArray arr2 = y.optJSONArray(i);
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
            Date date = df.parse(arr2.getString(6));

            model.add(new Cita( arr2.getInt(0),arr2.getInt(1),arr2.getInt(2),arr2.getString(3),arr2.getString(4),arr2.getString(5),date,arr2.getString(7),arr2.getString(8)));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_citasdoc, parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if(fecha.equals("hoy") || fecha.equals("citaspendientes")){
            holder.btnAceptar.setVisibility(View.VISIBLE);
            holder.btnCancelar.setVisibility(View.VISIBLE);
        }else {
            holder.btnAceptar.setVisibility(View.INVISIBLE);
            holder.btnCancelar.setVisibility(View.INVISIBLE);
        }

        String nombrepac = model.get(position).getNombrePac();
        String nombredoc = model.get(position).getNombreDoc();
        Date fechaCita = model.get(position).getFechaCita();
        String estado = model.get(position).getEstado();
        String especialidad = model.get(position).getEspecialidad();


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variables
                final String[] fechacita = {null};
                final String[] horacita = {null};

                final AlertDialog.Builder[] builder = {new AlertDialog.Builder(inflater.getContext(), R.style.CustomDialog)};
                View x = inflater.inflate(R.layout.alerta3,null);
                builder[0].setView(x);
                builder[0].create();
                final AlertDialog dialog =  builder[0].show();
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER_HORIZONTAL;
                dialog.getWindow().setLayout(1000, 900);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //botones
                Button btnaltacep = (Button)x.findViewById(R.id.BtnAltAcept);
                Button btnaltcancel = (Button)x.findViewById(R.id.BtnAltCancel);
                ImageView fecha  = x.findViewById(R.id.viewfecha);
                ImageView hora  = x.findViewById(R.id.viewhora);



                //fecha --------------------------
                fecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //iniciamos el calendario
                        Calendar fecha = Calendar.getInstance();
                        final int year = fecha.get(Calendar.YEAR);
                        final int month = fecha.get(Calendar.MONTH);
                        final int day = fecha.get(Calendar.DAY_OF_MONTH);

                        final DatePickerDialog dpd = new DatePickerDialog(inflater.getContext(), new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                Calendar c = Calendar.getInstance();
                                c.set(year, month+1, dayOfMonth);
                                int ndia =  c.get(Calendar.DAY_OF_WEEK);

                                if( ndia == 1 || ndia == 7 ){

                                    Toast.makeText(inflater.getContext(),"No trabajamos los fines de semana", Toast.LENGTH_SHORT).show();
                                }else{
                                    String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                    Toast.makeText(inflater.getContext(),date, Toast.LENGTH_SHORT).show();
                                    fechacita[0] = date;
                                }

                            }
                        },year,month,day);
                        dpd.show();
                    }
                });

                //hora --------------------------------
                hora.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar fecha = Calendar.getInstance();
                        final int hour = fecha.get(Calendar.HOUR_OF_DAY);
                        final int minute = fecha.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(inflater.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String date = hourOfDay+":"+minute+":00";
                                Toast.makeText(inflater.getContext(),date, Toast.LENGTH_SHORT).show();
                                horacita[0] = date;
                            }
                        },hour,minute,false);

                        timePickerDialog.show();
                    }
                });

                //bnt aceptar
                btnaltacep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        JSONObject jsonObject = new JSONObject();
                        try {

                            jsonObject.put("fecha"   ,fechacita[0]);
                            jsonObject.put("horacita", horacita[0]);
                            jsonObject.put("idcita", model.get(position).getId_cita());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if(fechacita[0] == null && horacita[0] == null){

                            Toast.makeText(inflater.getContext(),"No modificaste ningun dato.", Toast.LENGTH_SHORT).show();
                        }else{
                                if(fechacita[0] != null && horacita[0] != null ){

                                    try {
                                        jsonObject.put("tipo", "ambos");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    OkHttpClient client = new OkHttpClient();
                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                                    Request request = new Request.Builder()
                                            .url("https://juansuarezz-servidorflask.zeet.app/UpdatecitaDoc")
                                            .post(body)
                                            .build();

                                    Response response;
                                    try {
                                        response = client.newCall(request).execute();
                                        String resStr = Objects.requireNonNull(response.body()).string();



                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }else {
                                    if (fechacita[0] == null && horacita[0] != null) {
                                        try {
                                            jsonObject.put("tipo", "hora");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        OkHttpClient client = new OkHttpClient();
                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                                        Request request = new Request.Builder()
                                                .url("https://juansuarezz-servidorflask.zeet.app/UpdatecitaDoc")
                                                .post(body)
                                                .build();

                                        Response response;
                                        try {
                                            response = client.newCall(request).execute();
                                            String resStr = Objects.requireNonNull(response.body()).string();


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        if (fechacita[0] != null && horacita[0] == null) {
                                            try {
                                                jsonObject.put("tipo", "fecha");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            OkHttpClient client = new OkHttpClient();
                                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                                            Request request = new Request.Builder()
                                                    .url("https://juansuarezz-servidorflask.zeet.app/UpdatecitaDoc")
                                                    .post(body)
                                                    .build();

                                            Response response;
                                            try {
                                                response = client.newCall(request).execute();
                                                String resStr = Objects.requireNonNull(response.body()).string();


                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }
                                }
                        }
                        //actualizar-todoxd
                        CitasPenDocFragment activity = activityWeakReference.get();

                        activity.aux();
                        activity.startAsyncTask2();
                        dialog.dismiss();
                    }
                });



                //bnt cancelar
                btnaltcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


            }
        });

        //listener el bnt aceptar de la lista------------------------------------
        holder.btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //se crea la alerta
                final AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext(),R.style.CustomDialog);
                View x = inflater.inflate(R.layout.alerta1,null);
                final TextView descripcion = (TextView)x.findViewById(R.id.descripcion);
                Button btnaltacep = (Button)x.findViewById(R.id.BtnAltAcept);
                Button btnaltcancel = (Button)x.findViewById(R.id.BtnAltCancel);
                final TextView txtalerta = (TextView)x.findViewById(R.id.txtalerta);
                int color = Color.parseColor("#007BFF");
                txtalerta.setText("Vas a marcar esta Cita como Realizada");
                txtalerta.setTextColor(color);
                builder.setView(x);
                builder.create();
                final AlertDialog dialog =  builder.show();
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER_HORIZONTAL;
                dialog.getWindow().setLayout(1000, 900);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //boton aceptar alerta------------------------------------------------------
                btnaltacep.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (descripcion.getText().toString().isEmpty()) {
                                    descripcion.setError("DEBE RELLENAR ESTE CAMPO");
                                }else{

                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        //estado realizado
                                        jsonObject.put("estado", 2);
                                        jsonObject.put("descripcion", descripcion.getText());
                                        jsonObject.put("id_cita", model.get(holder.getAdapterPosition()).getId_cita());

                                        jsonObject.put("id_especialidad", model.get(holder.getAdapterPosition()).getEspecialidad());
                                        jsonObject.put("id_medico", model.get(holder.getAdapterPosition()).getId_medico());
                                        jsonObject.put("id_paciente", model.get(holder.getAdapterPosition()).getId_usuario());
                                        jsonObject.put("fecha", model.get(holder.getAdapterPosition()).getFechaCita());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    //aca hago el json
                                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                    okhttp3.Request request = new okhttp3.Request.Builder().url("https://juansuarezz-servidorflask.zeet.app/UpdateCita").post(body).build();

                                    okHttpClient.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                        }
                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {

                                        }
                                    });
                                    //cerramos la alerta
                                    dialog.dismiss();

                                    model.get(holder.getAdapterPosition()).setEstado("2");
                                    model.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());

                                }
                            }
                        }
                );

                //boton cancelar alerta----------------------------------------------------
                btnaltcancel.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        }
                );
            }
        });


        //listener el bnt cancelar de la lista-----------------------------------------------
        holder.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //se crea la alerta
                final AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext(),R.style.CustomDialog);
                View x = inflater.inflate(R.layout.alerta1,null);
                final TextView descripcion = (TextView)x.findViewById(R.id.descripcion);
                Button btnaltacep = (Button)x.findViewById(R.id.BtnAltAcept);
                Button btnaltcancel = (Button)x.findViewById(R.id.BtnAltCancel);
                final TextView txtalerta = (TextView)x.findViewById(R.id.txtalerta);
                int color = Color.parseColor("#FF0000");
                txtalerta.setText("Vas a marcar esta Cita como Cancelada");
                txtalerta.setTextColor(color);
                builder.setView(x);
                builder.create();
                final AlertDialog dialog =  builder.show();
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER_HORIZONTAL;
                dialog.getWindow().setLayout(1000, 900);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //boton aceptar alerta------------------------------------------------------
                btnaltacep.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (descripcion.getText().toString().isEmpty()) {
                                    descripcion.setError("DEBE RELLENAR ESTE CAMPO");
                                }else{

                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        //estado cancelado
                                        jsonObject.put("estado", 3);
                                        jsonObject.put("descripcion", descripcion.getText());
                                        jsonObject.put("id_cita", model.get(holder.getAdapterPosition()).getId_cita());

                                        jsonObject.put("id_especialidad", model.get(holder.getAdapterPosition()).getEspecialidad());

                                        jsonObject.put("id_medico", model.get(holder.getAdapterPosition()).getId_medico());
                                        jsonObject.put("id_paciente", model.get(holder.getAdapterPosition()).getId_usuario());
                                        jsonObject.put("fecha", model.get(holder.getAdapterPosition()).getFechaCita());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    //aca hago el json
                                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                    okhttp3.Request request = new okhttp3.Request.Builder().url("https://juansuarezz-servidorflask.zeet.app/UpdateCita").post(body).build();

                                    okHttpClient.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                        }
                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {

                                        }
                                    });
                                    //cerramos la alerta
                                    dialog.dismiss();

                                    model.get(holder.getAdapterPosition()).setEstado("3");
                                    model.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                }
                            }
                        }
                );

                //boton cancelar alerta----------------------------------------------------
                btnaltcancel.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        }
                );
            }
        });

        Color color = new Color();
        if(estado.equals("1")){
            estado = "cita pendiente";
            holder.estado.setText("Estado: "+ estado);
            holder.estado.setTextColor(Color.parseColor("#C0392B"));
        }else{
            estado = "cita ya Realizada";
            holder.estado.setText("Estado: "+ estado);
            holder.estado.setTextColor(Color.parseColor("#3498DB"));
        }

        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
        String fechaString = dateFormat.format(fechaCita);

        String[] parts2 = fechaString.split(" ");

        String fecha = parts2[0];
        fecha = fecha + " "+parts2[1]+" "+parts2[2]+" "+parts2[3];
        String hora = parts2[4];

        holder.nombrepaciente.setText("Doctor: "+nombredoc);
        holder.nombredoc.setText("Paciente: "+nombrepac);
        holder.fecha.setText("Fecha: "+fecha);
        holder.hora.setText("Hora: "+hora);
        holder.especialidad.setText("Especialidad: "+especialidad);
    }


    @Override
    public int getItemCount() {
        return model.size();
    }


    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    ///listener general para lo otro.
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }


    //clase holder
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombrepaciente,nombredoc, estado, fecha,hora,especialidad;
        ImageView  btnAceptar,btnCancelar,btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrepaciente  = itemView.findViewById(R.id.nombrepersonals);
            nombredoc       = itemView.findViewById(R.id.nombredoctorls);
            fecha           = itemView.findViewById(R.id.fechaCita);
            hora            = itemView.findViewById(R.id.horaCita);
            estado          = itemView.findViewById(R.id.Estado_cita);
            btnAceptar      = itemView.findViewById(R.id.btnRealizado);
            btnCancelar     = itemView.findViewById(R.id.btnCancelar);
            especialidad    = itemView.findViewById(R.id.Especialidad);
            btnEdit         = itemView.findViewById(R.id.btnEdit);
        }

    }




}
