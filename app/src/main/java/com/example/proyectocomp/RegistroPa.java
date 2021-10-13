package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class RegistroPa extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText txtnombre,txtedad,txtnombreU,txtcorreoE,txtpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pa);
        getSupportActionBar().hide();

        txtnombre = (EditText) findViewById(R.id.txtNombre);
        txtedad = (EditText) findViewById(R.id.txtEdadD);
        txtnombreU = (EditText) findViewById(R.id.txtnombreu);
        txtcorreoE = (EditText) findViewById(R.id.txtcorreoEP);
        txtpass = (EditText) findViewById(R.id.txtpass);
    }


    public void cancelar(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void volver(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registrarme(View view){

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
        //fin if
        } else{

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("nombre", txtnombre.getText());
                jsonObject.put("edad", txtedad.getText());
                jsonObject.put("fechacreacion", Calendar.getInstance().getTime());
                jsonObject.put("correo", txtcorreoE.getText());
                jsonObject.put("fechaultimacita", Calendar.getInstance().getTime());
                jsonObject.put("nombreu", txtnombreU.getText());
                jsonObject.put("contrasena", txtpass.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient okHttpClient = new OkHttpClient();
            //aca hago el json
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            okhttp3.Request request = new okhttp3.Request.Builder().url("https://juansuarezz-servidorflask.zeet.app/registrarP").post(body).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    volver();
                }
            });
        }
        ///fin enviar Json
    }




}