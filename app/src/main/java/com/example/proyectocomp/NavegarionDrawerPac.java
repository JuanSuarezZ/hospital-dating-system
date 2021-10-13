package com.example.proyectocomp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.Doctor;
import com.example.proyectocomp.Entidades.pacientes;
import com.example.proyectocomp.Fragments.CitasPenDocFragment;
import com.example.proyectocomp.Fragments.historiamFragment;
import com.example.proyectocomp.FragmentsPac.HistorialMedico;
import com.example.proyectocomp.FragmentsPac.PedirCita;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class NavegarionDrawerPac extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    //variables del menu Drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Toolbar toolbar;

    //variables para el fragment
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView nombredelDoc,correodelDoc;


    // pacientes paciente;
    pacientes paciente;
    ArrayList<Cita> listacitas;
    ArrayList<Doctor> listaDoctores;
    ArrayList<String>  listaEspecialidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegarion_drawer_pac);

        //toolbar
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //drawer
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationViewPac);

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


        //cargaPantallaInicial
        //carga del fragment principal----------------------------------------------------------------
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //intent
        listacitas = new ArrayList<>();
        listaDoctores = new ArrayList<>();
        listaEspecialidad = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            paciente = (pacientes) getIntent().getSerializableExtra("paciente");
            listacitas =  (ArrayList<Cita>) getIntent().getSerializableExtra("listaCitas");
            listaEspecialidad =  (ArrayList<String>) getIntent().getSerializableExtra("listaespecialidades");
            listaDoctores =  (ArrayList<Doctor>) getIntent().getSerializableExtra("listaDoctores");
        }

        //set data
        nombredelDoc.setText(paciente.getNombre());
        correodelDoc.setText(paciente.getCorreo());

        Bundle arguments = new Bundle();

        arguments.putSerializable("paciente", paciente);
        arguments.putSerializable("listaEspecialidad",listaEspecialidad);


        PedirCita pedirCita = new PedirCita();
        pedirCita.setArguments(arguments);

        getSupportActionBar().setTitle("Citas");

        fragmentTransaction.add(R.id.container,pedirCita);
        fragmentTransaction.commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        if(item.getItemId() == R.id.historia){

            getSupportActionBar().setTitle("Historial medico");
            fragmentManager = getSupportFragmentManager();

            fragmentTransaction = fragmentManager.beginTransaction();
            Bundle arguments = new Bundle();
            arguments.putSerializable("paciente", paciente);
            arguments.putSerializable("listaCitas",listacitas);
            arguments.putSerializable("listaEspecialidad",listaEspecialidad);
            arguments.putSerializable("listaDoctores",listaDoctores);

            HistorialMedico historialMedico = new HistorialMedico();
            historialMedico.setArguments(arguments);

            fragmentTransaction.replace(R.id.container,historialMedico);
            fragmentTransaction.commit();
        }

        if(item.getItemId() == R.id.Citas){

            getSupportActionBar().setTitle("Citas");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            Bundle arguments = new Bundle();

            arguments.putSerializable("paciente", paciente);
            arguments.putSerializable("listaEspecialidad",listaEspecialidad);
            arguments.putSerializable("listaDoctores",listaDoctores);

            PedirCita pedirCita = new PedirCita();
            pedirCita.setArguments(arguments);

            fragmentTransaction.replace(R.id.container,pedirCita);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.Exit){
            cerrarsesionPac();
        }
        return false;
    }

    public void cerrarsesionPac(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userN","no hay info");
        editor.putString("pass","no hay info");
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
