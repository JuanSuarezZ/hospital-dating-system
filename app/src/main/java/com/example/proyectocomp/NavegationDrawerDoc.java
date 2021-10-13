package com.example.proyectocomp;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.pacientes;
import com.example.proyectocomp.Fragments.CitasPenDocFragment;
import com.example.proyectocomp.Fragments.MainFragment;
import com.example.proyectocomp.Fragments.historiamFragment;
import com.github.nkzawa.emitter.Emitter;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


public class NavegationDrawerDoc extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {


    //variables socket
    private EditText mInputMessageView;

    //contador onBackPressed()
    int counter = 0;

    //variables del fragment
    TextView nombredelDoc,correodelDoc;

    //variables del menu Drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Toolbar toolbar;

    //variables del medico
    String userN,pass,anose,correo,direccion,edad,fechaingreso,id,lugargrado,mensaje,nombre;
    ArrayList<Cita> listacitas;

    //variables pacientes
    ArrayList<pacientes> listaPacientes;

    //variables para el fragment
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegation_drawer_doc);

        //toolbar
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Citas Pendientes");

        //drawer
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        //evento onclick al navegaitonview
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // header del menuDrawer
        View header = navigationView.getHeaderView(0);

        nombredelDoc = header.findViewById(R.id.textViewNombreDoc);
        correodelDoc = header.findViewById(R.id.textViewCorreoDoc);

        //datos del anterior Activity
        listacitas = new ArrayList<>();
        listaPacientes = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            userN = bundle.getString("userN");
            pass = bundle.getString("pass");
            id = bundle.getString("id");
            nombre = bundle.getString("nombre");
            edad = bundle.getString("edad");
            direccion = bundle.getString("direccion");
            correo = bundle.getString("correo");
            anose = bundle.getString("anose");
            lugargrado = bundle.getString("lugargrado");
            fechaingreso = bundle.getString("fechaingreso");
            listacitas =  (ArrayList<Cita>) getIntent().getSerializableExtra("listaCitas");
            listaPacientes =  (ArrayList<pacientes>) getIntent().getSerializableExtra("listaPacientes");

            nombredelDoc.setText( userN );
            correodelDoc.setText(correo);
        }

        //carga del fragment principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Bundle arguments = new Bundle();

        arguments.putString("userN", userN);
        arguments.putString("pass", pass);
        arguments.putString("id", id);
        arguments.putString("nombre", nombre);
        arguments.putString("edad", edad);
        arguments.putString("direccion", direccion);
        arguments.putString("correo", correo);
        arguments.putString("anose", anose);
        arguments.putString("lugargrado", lugargrado);
        arguments.putString("fechaingreso", fechaingreso);
        arguments.putSerializable("listaCitas",listacitas);

        CitasPenDocFragment citasPenDocFragment = new CitasPenDocFragment();
        citasPenDocFragment.setArguments(arguments);
        fragmentTransaction.add(R.id.container,citasPenDocFragment);
        fragmentTransaction.commit();

    }//finnnnnnnnnnnnnnnnnnnnn onCreate()


    public void cerrarsesionDoc(){
        SharedPreferences preferences = getSharedPreferences("credencialesDoc", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userN","no hay info");
        editor.putString("pass","no hay info");
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //seleccion de rutas en el menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        if(item.getItemId() == R.id.historia){

            getSupportActionBar().setTitle("Historias Medicas");
            fragmentManager = getSupportFragmentManager();

            Bundle arguments = new Bundle();
            arguments.putString("nombreU", userN);
            arguments.putString("pass", pass);
            arguments.putString("id", id);
            arguments.putString("nombre", nombre);
            arguments.putString("edad", edad);
            arguments.putString("direccion", direccion);
            arguments.putString("correo", correo);
            arguments.putString("anose", anose);
            arguments.putString("lugargrado", lugargrado);
            arguments.putString("fechaingreso", fechaingreso);
            arguments.putSerializable("listaPacientes",listaPacientes);

            historiamFragment historiamFragment = new historiamFragment();
            historiamFragment.setArguments(arguments);

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,historiamFragment);
            fragmentTransaction.commit();

        }
        if(item.getItemId() == R.id.Citas){

            getSupportActionBar().setTitle("Citas");
            fragmentManager = getSupportFragmentManager();

            Bundle arguments = new Bundle();
            arguments.putString("nombreU", userN);
            arguments.putString("pass", pass);
            arguments.putString("id", id);
            arguments.putString("nombre", nombre);
            arguments.putString("edad", edad);
            arguments.putString("direccion", direccion);
            arguments.putString("correo", correo);
            arguments.putString("anose", anose);
            arguments.putString("lugargrado", lugargrado);
            arguments.putString("fechaingreso", fechaingreso);
            arguments.putSerializable("listaCitas",listacitas);

            CitasPenDocFragment citasPenDocFragment = new CitasPenDocFragment();
            citasPenDocFragment.setArguments(arguments);

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, citasPenDocFragment);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.Exit){
            cerrarsesionDoc();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        counter++;
        if(counter == 2){
            super.onBackPressed();
            this.finish();
        }
    }

    //fin
}