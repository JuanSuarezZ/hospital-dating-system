package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class loginDoc extends AppCompatActivity {

    private static final int LONG_DELAY = 1035000;
    EditText txtnombreu,txtpass;
    TextView txtSesionini;
    Button b1,b2,b3,b4;
    String mensaje;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doc);
        getSupportActionBar().hide();
        txtnombreu = (EditText) findViewById(R.id.txtxNombreUD);
        txtpass    = (EditText) findViewById(R.id.txtcontraseñaD);
        txtSesionini = (TextView) findViewById(R.id.txtSesionIni);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            mensaje = bundle.getString("mensaje");
            for (int i=0; i < 3; i++)
            {
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
            }

        }

        b1 =  findViewById(R.id.button2);
        b2 =  findViewById(R.id.button3);
        b3 =  findViewById(R.id.button4);
        b4 = findViewById(R.id.btnRegistroDoc);

        cargarpreferencias();

    }//fin Oncreate();;;;;;;;;


    public void iniciarSecion(View view){
        if (txtnombreu.getText().toString().isEmpty()) {
            txtnombreu.setError("DEBE RELLENAR ESTE CAMPO");
        } else if (txtpass.getText().toString().isEmpty()) {
            txtpass.setError("DEBE RELLENAR ESTE CAMPO");
        }else{
            OkHttpClient client = new OkHttpClient();
            String url = "https://juansuarezz-servidorflask.zeet.app/loginD?nombreu="+txtnombreu.getText().toString()+"&contra="+txtpass.getText().toString();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        loginDoc.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(myResponse.equals("valido")){
                                    DoctorRegistrado( txtnombreu.getText().toString(), txtpass.getText().toString());
                                    guardarpreferenciasDoc(txtnombreu.getText().toString(),txtpass.getText().toString());
                                } else if(myResponse.equals("no existe")){
                                    txtnombreu.setError("Usuario no encontrado");
                                } else{
                                    txtpass.setText("");
                                    txtpass.setError("Contraseña incorrecta");
                                }
                            }
                        });
                    }
                }
            });

        }
    }//fin IniciarSecion


    public void iniciarSecionAuto(final String Nusuario, final String pass){

            OkHttpClient client = new OkHttpClient();
            String url = "https://juansuarezz-servidorflask.zeet.app/loginD?nombreu="+Nusuario+"&contra="+pass;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().string();
                        loginDoc.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(myResponse.equals("valido")){
                                    DoctorRegistrado( Nusuario,  pass);
                                }
                            }
                        });
                    }
                }
            });
    }

    public void guardarpreferenciasDoc(String Nusuario, String pass){
        SharedPreferences preferences = getSharedPreferences("credencialesDoc", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userN",Nusuario);
        editor.putString("pass",pass);
        editor.commit();
    }

    public void cargarpreferencias(){
        SharedPreferences preferences = getSharedPreferences("credencialesDoc", Context.MODE_PRIVATE);
        String user = preferences.getString("userN","no hay info");
        String pass = preferences.getString("pass","no hay info");

        if(user.equals("no hay info") || pass.equals("no hay info") ){
            //no se hace nada
        }else{
            txtnombreu.setVisibility(View.INVISIBLE);
            txtpass.setVisibility(View.INVISIBLE);
            b1.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.INVISIBLE);
            b4.setVisibility(View.INVISIBLE);
            txtSesionini.setVisibility(View.VISIBLE);

            iniciarSecionAuto(user,pass);
        }
    }

    public void DoctorRegistrado(String Nusuario, String pass){
        Intent intent = new Intent(this, panCarga.class);
        intent.putExtra("identificador","Doctor");
        intent.putExtra("userN",Nusuario.trim());
        intent.putExtra("pass" ,pass.trim());
        startActivity(intent);
        finish();
    }

    public void cancelar(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void recuperarContraseña(View view){
        Intent intent = new Intent(this, RecuperacionM.class);
        startActivity(intent);

    }

    public void aRegistroDoc(View view){
        Intent intent = new Intent(this, RegistroDoc.class);
        startActivity(intent);
    }


}