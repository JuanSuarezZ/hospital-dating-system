package com.example.proyectocomp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextInputLayout txtpass,txtnombreu;
    TextView txtadmin;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        requestQueue = Volley.newRequestQueue(this);

        txtnombreu = findViewById(R.id.txtnombre);
        txtpass    = findViewById(R.id.txtcontraseña);
        txtadmin = findViewById(R.id.btnAdmin);

        txtadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(getBaseContext(), loginAdmin.class);
               startActivity(intent);
                finish();
            }
        });


        //permisos
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //inicio de sesion automatico
        cargarpreferencias();
    }

    public void cargarpreferencias(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String user = preferences.getString("userN","no hay info");
        String pass = preferences.getString("pass","no hay info");

        assert user != null;
        assert pass != null;
        if(user.equals("no hay info") || pass.equals("no hay info") ){
            //no se hace nada
        }else{
            iniciarSecionAuto(user,pass);
        }
    }

    public void guardarpreferencias(String Nusuario, String pass){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userN",Nusuario);

        editor.putString("pass",pass);
        editor.apply();
    }

    public void iniciarSecionAuto(String Nuser, String txtconta){
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("nombreu",Nuser);
            jsonObject.put("contra", txtconta);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url("https://juansuarezz-servidorflask.zeet.app/login")
                .post(body)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            String resStr = Objects.requireNonNull(response.body()).string();

            if(resStr.equals("valido")){
                PacienteRegistrado(Nuser,txtconta);

            } else if(resStr.equals("no existe")){
                txtnombreu.setError("Usuario no encontrado");
            } else{
                txtnombreu.getEditText().setText("");
                txtpass.setError("Contraseña incorrecta");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void iniciarSecion(View view){
        txtnombreu.setError(null);
        txtpass.setError(null);

        if( txtnombreu.getEditText().getText().toString().isEmpty() ){
            txtnombreu.setError("ingrese un nombre de usuario");
        }else {
            if( txtpass.getEditText().getText().toString().isEmpty() ){
                txtpass.setError("ingrese una contraseña");
            }else {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("nombreu", txtnombreu.getEditText().getText().toString());
            jsonObject.put("contra", txtpass.getEditText().getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url("https://juansuarezz-servidorflask.zeet.app/login")
                .post(body)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            String resStr = Objects.requireNonNull(response.body()).string();

            if(resStr.equals("valido")){
                PacienteRegistrado(txtnombreu.getEditText().getText().toString(),txtpass.getEditText().getText().toString());
                guardarpreferencias(txtnombreu.getEditText().getText().toString(),txtpass.getEditText().getText().toString());
            } else if(resStr.equals("no existe")){
                txtnombreu.setError("Usuario no encontrado");
            } else{
                txtpass.getEditText().setText("");
                txtpass.setError("Contraseña incorrecta");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

            }

        }

    }

    public void aRegistro(View view){
        Intent intent = new Intent(this, RegistroPa.class);
        startActivity(intent);
    }

    public void PacienteRegistrado(String Nuser,String txtpass){
        Intent intent = new Intent(this, panCarga.class);
        intent.putExtra("identificador","Paciente");
        intent.putExtra("userN",Nuser);
        intent.putExtra("pass" ,txtpass);
        finish();
        startActivity(intent);
    }

    public void loginDoctors(View view){
        Intent intent = new Intent(this, loginDoc.class);
        startActivity(intent);
    }

    public void recuperateContrasP(View view){
        Intent intent = new Intent(this, RecuperacionP.class);
        startActivity(intent);
    }


}