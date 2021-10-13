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

public class RecuperacionP extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText txtCorreoRP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion_p);
        getSupportActionBar().hide();
        requestQueue = Volley.newRequestQueue(this);

        txtCorreoRP = (EditText) findViewById(R.id.txtCorreoRP);

    }

    public void RecuperarCuenta(View view) {
        if (txtCorreoRP.getText().toString().isEmpty()) {
            txtCorreoRP.setError("DEBE RELLENAR ESTE CAMPO");
        } else {

            String URL = "https://juansuarezz-servidorflask.zeet.app/recuperacionP?correo=" + txtCorreoRP.getText().toString();
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
                                    txtCorreoRP.setError("Correo no valido");
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
        Intent intent = new Intent(this, validarCodigo2.class);
        intent.putExtra("cod", cod.trim());
        intent.putExtra("id", id.toString().trim());
        startActivity(intent);
    }

    public void cancelar(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}