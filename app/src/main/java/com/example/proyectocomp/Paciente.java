package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Paciente extends AppCompatActivity implements View.OnClickListener {
    Button btnlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
        getSupportActionBar().hide();
        btnlogout  = (Button)   findViewById(R.id.btnCerrarsesionP);
        btnlogout.setOnClickListener(this);
    }

    public void cerrarsesion(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userN","no hay info");
        editor.putString("pass","no hay info");
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnlogout.getId()){
            cerrarsesion();
        }

    }


}