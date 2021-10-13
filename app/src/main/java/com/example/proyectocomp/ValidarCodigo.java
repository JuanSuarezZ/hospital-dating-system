package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ValidarCodigo extends AppCompatActivity {
    String cod,id;
    EditText txtCodigo3;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_codigo);
        getSupportActionBar().hide();
        requestQueue = Volley.newRequestQueue(this);
        txtCodigo3 = (EditText) findViewById(R.id.txtcodigo);

        Bundle bundle = getIntent().getExtras();
        cod = bundle.getString("cod");
        id  = bundle.getString("id");
    }

    public void cancelar(View view){
        Intent intent = new Intent(this, RecuperacionP.class);
        startActivity(intent);
    }

    public void validar(View view){

        String aux = txtCodigo3.getText().toString();
        if(aux.equals(cod)){
            Intent intent = new Intent(this, CambiarContra.class);
            intent.putExtra("id", id.trim());
            startActivity(intent);
        }
    }

}