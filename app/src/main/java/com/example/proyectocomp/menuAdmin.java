package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.proyectocomp.Entidades.pacientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class menuAdmin extends AppCompatActivity {

    CardView reportes1, reportes2, reportes3, reportes4;
    String userN;
    RequestQueue requestQueue;
    TextView nombreAdmin,UltimaCAdmin,rolAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_admin);
        //ocultar barrra
        Objects.requireNonNull(getSupportActionBar()).hide();
        requestQueue = Volley.newRequestQueue(this);

        nombreAdmin  = findViewById(R.id.nombreAdmin);
        UltimaCAdmin = findViewById(R.id.UltimaCAdmin);
        rolAdmin = findViewById(R.id.rolAdmin);

        reportes1 = findViewById(R.id.card1);
        reportes2 = findViewById(R.id.card2);
        reportes3 = findViewById(R.id.card3);
        reportes4 = findViewById(R.id.card4);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            userN = bundle.getString("nombre");
        }


        reportes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(v.getContext(), reportes1.class));
                Animatoo.animateSlideLeft(v.getContext());

            }
        });

        reportes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), panCarga.class);
                intent.putExtra("identificador", "reporte2");
                startActivity(intent);
                Animatoo.animateSlideLeft(v.getContext());

            }
        });

        reportes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(v.getContext(), reportes3.class));
                Animatoo.animateSlideLeft(v.getContext());

            }
        });

        cargarData();

    }

    private void cargarData() {

            String URL = "https://juansuarezz-servidorflask.zeet.app/verDataAdmin?nombre=" + userN;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, URL, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String nombre,ultimaconexion,rol;
                            try {

                                nombre = response.getString("nombre");
                                ultimaconexion = response.getString("ultimaconexion");
                                rol = response.getString("rol");

                                nombreAdmin.setText(nombreAdmin.getText()+": "+nombre);
                                UltimaCAdmin.setText(UltimaCAdmin.getText()+" "+ultimaconexion);
                                rolAdmin.setText(rolAdmin.getText()+" "+rol);


                                System.out.println(nombre+ultimaconexion+rol);



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