package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectocomp.Entidades.Especialidades;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class RegistroDoc extends AppCompatActivity {

    EditText txtnombre,txtedad,txtnombreU,txtcorreoE,txtpass,txtdireccion,txtañosE,txtlugargrado;
    RequestQueue requestQueue;
    ArrayList<Especialidades> lista;
    Button btnsiguiente;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_doc);
        requestQueue = Volley.newRequestQueue(this);
        getSupportActionBar().hide();

        txtnombre = (EditText) findViewById(R.id.txtNombre);
        txtedad = (EditText) findViewById(R.id.txtEdadD);
        txtnombreU = (EditText) findViewById(R.id.txtnombreu);
        txtcorreoE = (EditText) findViewById(R.id.txtCorreo);
        txtpass = (EditText) findViewById(R.id.txtcontrasena);
        txtdireccion = (EditText) findViewById(R.id.txtDireccion);
        txtañosE =  (EditText) findViewById(R.id.txtanose);

        txtlugargrado =   (EditText) findViewById(R.id.txtlugargrado);
        btnsiguiente = (Button) findViewById(R.id.btnRegistro);
        //btnsiguiente.setVisibility(View.INVISIBLE);
        lista = new ArrayList<Especialidades>();

        //carga lista de especilidades
        cargarEspecialidades();
    }

    private void cargarEspecialidades() {

        String URL = "https://juansuarezz-servidorflask.zeet.app/verEspecialidades";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String cod,id,mensaje;
                        try {

                            JSONArray x = new JSONArray();
                            JSONArray y = new JSONArray();
                            JSONObject z = new JSONObject();

                            x = response.getJSONArray("nombres");

                            for( int i = 0; i<x.length() ; i++ ){
                                JSONArray arr2 = x.optJSONArray(i);

                                Especialidades aux = new Especialidades(arr2.getInt(0),arr2.getString(1));
                                lista.add(aux);
                            }
                            btnsiguiente.setVisibility(View.VISIBLE);
                            } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        requestQueue.add(jsonObjectRequest);

    }

    public void volver(View view){
        Intent intent = new Intent(this, loginDoc.class);
        startActivity(intent);
    }

    public void volver2(View view){

        if (txtnombre.getText().toString().isEmpty()) {
            txtnombre.setError("DEBE RELLENAR ESTE CAMPO");
        } else if (txtedad.getText().toString().isEmpty()) {
            txtedad.setError("DEBE RELLENAR ESTE CAMPO");
        } else if (txtnombreU.getText().toString().isEmpty()){
            txtnombreU.setError("DEBE RELLENAR ESTE CAMPO");
        } else if(txtcorreoE.getText().toString().isEmpty()){
            txtcorreoE.setError("DEBE RELLENAR ESTE CAMPO");
        } else if(txtpass.getText().toString().isEmpty()){
            txtpass.setError("DEBE RELLENAR ESTE CAMPO");
        } else if(txtdireccion.getText().toString().isEmpty()){
            txtdireccion.setError("DEBE RELLENAR ESTE CAMPO");
        } else if(txtañosE.getText().toString().isEmpty()){
            txtañosE.setError("DEBE RELLENAR ESTE CAMPO");
        } else if(txtlugargrado.getText().toString().isEmpty()){
            txtlugargrado.setError("DEBE RELLENAR ESTE CAMPO");
        } else{

            Intent intent = new Intent(this,SelectEspecialidades.class);

            intent.putExtra("nombre", txtnombre.getText().toString());
            intent.putExtra("edad", txtedad.getText().toString());
            intent.putExtra("direccion", txtdireccion.getText().toString());
            intent.putExtra("correo", txtcorreoE.getText().toString());
            intent.putExtra("anose", txtañosE.getText().toString());
            intent.putExtra("lugargrado", txtlugargrado.getText().toString());
            intent.putExtra("fechaingreso",Calendar.getInstance().getTime().toString());
            intent.putExtra("nombreu", txtnombreU.getText().toString());
            intent.putExtra("contrasena", txtpass.getText().toString());
            intent.putExtra("lista", lista);

            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        counter++;
        if(counter == 2){
            super.onBackPressed();
            this.finish();
        }
    }





}