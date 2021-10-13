package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.Objects;

public class reportes3 extends AppCompatActivity {

    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes3);
        //ocultar barrra
        Objects.requireNonNull(getSupportActionBar()).hide();

    }


    @Override
    public void onBackPressed() {

        counter++;
        if(counter == 2){

            startActivity(new Intent(this, menuAdmin.class));
            Animatoo.animateSlideDown((this));

            this.finish();
        }
    }


}