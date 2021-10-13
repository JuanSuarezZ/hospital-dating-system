package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class RecuperacionM extends AppCompatActivity {
    RequestQueue requestQueue;
    EditText txtCorreoRM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion_m);
        getSupportActionBar().hide();
        requestQueue = Volley.newRequestQueue(this);

        txtCorreoRM = (EditText) findViewById(R.id.txtCorreoRM);
    }

    public void RecuperarCuenta(View view) {
        if (txtCorreoRM.getText().toString().isEmpty()) {
            txtCorreoRM.setError("DEBE RELLENAR ESTE CAMPO");
        } else {

            String URL = "https://juansuarezz-servidorflask.zeet.app/recuperacionM?correo=" + txtCorreoRM.getText().toString();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, URL, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String cod,id,mensaje;
                            try {
                                mensaje = response.getString("mensaje");
                                if(mensaje.equals("existe")){
                                    cod = response.getString("cod");
                                    id = response.getString("id");
                                    irAvalidar(cod,id);
                                }else{
                                    txtCorreoRM.setError("Correo no valido");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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
    }

    public void irAvalidar(String cod,String id){
        Intent intent = new Intent(this, ValidarCodigo.class);
        intent.putExtra("cod", cod.trim());
        intent.putExtra("id", id.toString().trim());
        startActivity(intent);
    }

    public void cancelar(View view){
        Intent intent = new Intent(this, loginDoc.class);
        startActivity(intent);
    }



}