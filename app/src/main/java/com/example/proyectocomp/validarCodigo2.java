package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class validarCodigo2 extends AppCompatActivity {
    String cod,id;
    EditText txtcodigo;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_codigo2);
        getSupportActionBar().hide();
        requestQueue = Volley.newRequestQueue(this);
        txtcodigo = (EditText) findViewById(R.id.txtCodigo3P);

        Bundle bundle = getIntent().getExtras();
        cod = bundle.getString("cod");
        id = bundle.getString("id");
    }

    public void cancelar(View view){
        Intent intent = new Intent(this, RecuperacionP.class);
        startActivity(intent);
    }

    public void validar(View view){

        String aux = txtcodigo.getText().toString();
        if(aux.equals(cod)){
            Intent intent = new Intent(this, cambiarContra2.class);
            intent.putExtra("id", id.trim());
            startActivity(intent);
        }
    }



}