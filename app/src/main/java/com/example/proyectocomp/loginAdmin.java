package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginAdmin extends AppCompatActivity {

    int counter = 0;
    EditText txtpass,txtnombreu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_admin);
        //ocultar barrra
        Objects.requireNonNull(getSupportActionBar()).hide();

        txtnombreu = findViewById(R.id.nombrelogadmin);
        txtpass    = findViewById(R.id.passlogadmin);
    }

    public void iniciarSecion(View view){

        txtnombreu.setError(null);
        txtpass.setError(null);

        if( txtnombreu.getText().toString().isEmpty() ){
            txtnombreu.setError("ingrese un nombre de usuario");
        }else {
            if( txtpass.getText().toString().isEmpty() ){
                txtpass.setError("ingrese una contraseña");
            }else {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("nombreu", txtnombreu.getText().toString());
                    jsonObject.put("contra", txtpass.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                Request request = new Request.Builder()
                        .url("https://juansuarezz-servidorflask.zeet.app/logAdmin")
                        .post(body)
                        .build();

                Response response;
                try {
                    response = client.newCall(request).execute();
                    String resStr = Objects.requireNonNull(response.body()).string();

                    if(resStr.equals("valido")){

                        logueado(txtnombreu.getText().toString());
                        //PacienteRegistrado(txtnombreu.getEditText().getText().toString(),txtpass.getEditText().getText().toString());
                        //guardarpreferencias(txtnombreu.getEditText().getText().toString(),txtpass.getEditText().getText().toString());

                    } else if(resStr.equals("no existe")){
                        txtnombreu.setError("Usuario no encontrado");
                    } else{
                        txtpass.setText("");
                        txtpass.setError("Contraseña incorrecta");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void logueado(String s){
        Intent intent = new Intent(this, menuAdmin.class);
        intent.putExtra("nombre",s);
        startActivity(intent);
        finish();
    }

    public void volver(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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