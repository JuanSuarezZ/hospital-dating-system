package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.Especialidades;
import com.example.proyectocomp.adaptadores.AdapterEspecialidad;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class SelectEspecialidades extends AppCompatActivity {


    ArrayList<Especialidades> lista = new ArrayList<>();
    ArrayList<String> listaux = new ArrayList<>();
    ArrayList<Especialidades> listalocal;
    RecyclerView recyclerView;
    String txtnombre,txtedad,txtnombreU,txtcorreoE,txtpass,txtdireccion,txtañosE,txtlugargrado,fecha;
    LinearLayoutManager mLinearLayoutManager;
    Button continuar,cancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_especialidades);
        getSupportActionBar().hide();

        continuar = (Button) findViewById(R.id.BtcSiguiente);
        cancelar = (Button) findViewById(R.id.btncancelar6);
        recyclerView = (RecyclerView) findViewById(R.id.recycler2);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            lista =  (ArrayList<Especialidades>) getIntent().getSerializableExtra("lista");

            txtnombre = bundle.getString("nombre");
            txtedad = bundle.getString("edad");
            txtdireccion = bundle.getString("direccion");
            txtcorreoE = bundle.getString("correo");
            txtañosE = bundle.getString("anose");
            txtlugargrado = bundle.getString("lugargrado");
            fecha = bundle.getString("fechaingreso");
            txtnombreU = bundle.getString("nombreu");
            txtpass = bundle.getString("contrasena");
        }

        registrarDoc();

        final AdapterEspecialidad adapter = new AdapterEspecialidad(lista,recyclerView);
        recyclerView.setAdapter(adapter);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listalocal = adapter.getlista();

                //Toast toast1 = Toast.makeText(getBaseContext(), "Doctor Registrado!", Toast.LENGTH_SHORT);
                agregarEsp(listalocal);
                //toast1.show();
                volverRegistrado();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });


    }



    public void registrarDoc(){

            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("nombre", txtnombre);
                jsonObject.put("edad", txtedad);
                jsonObject.put("direccion", txtdireccion);
                jsonObject.put("correo", txtcorreoE);
                jsonObject.put("anose", txtañosE);
                jsonObject.put("lugargrado", txtlugargrado);
                jsonObject.put("fechaingreso",fecha);
                jsonObject.put("nombreu", txtnombreU);
                jsonObject.put("contrasena", txtpass);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient okHttpClient = new OkHttpClient();
            //aca hago el json
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            final okhttp3.Request request = new okhttp3.Request.Builder().url("https://juansuarezz-servidorflask.zeet.app/registrarDoc").post(body).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    String respuesta = response.body().string();

                    if(respuesta.equals("No hay mas cupo para doctores")){
                        volver();
                    }else{

                    }

                }
            });


        ///fin enviar Json
    }

    public void volverRegistrado(){

        Intent intent = new Intent(this,loginDoc.class);
        intent.putExtra("mensaje","Se Ha Registrado como doctor :)");
        finish();
        startActivity(intent);
    }

    public void volver(){

        Intent intent = new Intent(this,loginDoc.class);
        intent.putExtra("mensaje","No hay cupos para mas doctores");
        finish();
        startActivity(intent);
    }


    public void agregarEsp(ArrayList<Especialidades> listalocal){


        for (int i=0; i<listalocal.size() ; i++){
                listaux.add(listalocal.get(i).getId()+"");
            }

            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray list = new JSONArray(listaux);
                jsonObject.put("data", list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient okHttpClient = new OkHttpClient();
            //aca hago el json
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            final okhttp3.Request request = new okhttp3.Request.Builder().url("https://juansuarezz-servidorflask.zeet.app/RegistroEsp").post(body).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                   // String respuesta = response.body().string();
                }
            });
        }
        ///fin enviar Json






    }






