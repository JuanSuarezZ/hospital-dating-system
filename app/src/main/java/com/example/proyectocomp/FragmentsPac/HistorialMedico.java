package com.example.proyectocomp.FragmentsPac;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomp.Entidades.Cita;
import com.example.proyectocomp.Entidades.Doctor;
import com.example.proyectocomp.Entidades.pacientes;
import com.example.proyectocomp.R;
import com.example.proyectocomp.adaptadores.AdapterCitaMedico;
import com.example.proyectocomp.adaptadores.AdapterEspecPac;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HistorialMedico  extends Fragment {

    // pacientes paciente;
    pacientes paciente;
    ArrayList<Cita> listacitas = new ArrayList<>();
    ArrayList<Cita> listacitasaux = new ArrayList<>();
    //recycler
    RecyclerView recyclerView1;
    LinearLayoutManager mLinearLayoutManager;
    AdapterCitaMedico adapterCitaMedico;

    //pdf
    String directorio = "MisPdfa";
    String nombredocumento = "MihistoriaMedica.pdf";

    //listas
    ArrayList<String>  listaEspecialidad;
    ArrayList<String>  nombres;
    ArrayList<String>  aux = new ArrayList<>();
    ArrayList<Doctor>  listaDoctores;
    Spinner spinnerEspecialidad,spinerDoctores;

    //imageview
    ImageView calendar,buscar;

    //strings
    String dateSelected = "null";

    //int
    int especialidadSelected = 0 ,doctorSelected = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_historiamedipac, container, false);

        //listas
        listaEspecialidad = new ArrayList<>();
        listaDoctores = new ArrayList<>();
        nombres = new ArrayList<>();


        //imageview
        calendar = view.findViewById(R.id.calendar);
        buscar   = view.findViewById(R.id.search);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            listacitas   = (ArrayList<Cita>) bundle.getSerializable("listaCitas");
            listaEspecialidad   = (ArrayList<String>) bundle.getSerializable("listaEspecialidad");
            listaDoctores   = (ArrayList<Doctor>) bundle.getSerializable("listaDoctores");
            paciente  = (pacientes) bundle.getSerializable("paciente");
        }

        // spiner doctores
        aux.add("ninguno");
        for(int i =0 ; i< listaDoctores.size() ; i++){

            aux.add(listaDoctores.get(i).getNombre());
        }

        spinerDoctores = view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapterSD = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,aux);
        spinerDoctores.setAdapter(adapterSD);

        spinerDoctores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doctorSelected = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spiner especialidad
        aux = new ArrayList<>();
        aux.add("ninguno");
        for(int i =0 ; i< listaEspecialidad.size() ; i++){

            aux.add(listaEspecialidad.get(i));
        }

        spinnerEspecialidad = view.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,aux);
        spinnerEspecialidad.setAdapter(adapter);

        spinnerEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                especialidadSelected = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //recycler historia medica
        recyclerView1 = view.findViewById(R.id.recyclerHistoriaMedica);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(mLinearLayoutManager);
        adapterCitaMedico = new AdapterCitaMedico(listacitas, recyclerView1,"paciente");
        recyclerView1.setAdapter(adapterCitaMedico);

        // Permisos
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }

        //menu float
        final FloatingActionMenu menu = view.findViewById(R.id.fabmenu);
        menu.setClosedOnTouchOutside(true);

        //boton floating
        FloatingActionButton myFab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        myFab2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //genera documento
                menu.close(true);
                crearPDF();
                Toast.makeText(getContext(), "SE HA GUARDADO COMO PDF", Toast.LENGTH_LONG).show();

            }
        });

        //boton floating
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //genera documento
                menu.close(true);
                adapterCitaMedico.setLista(listacitas);
                adapterCitaMedico.notifyDataSetChanged();
                Toast.makeText(getContext(), "restart", Toast.LENGTH_LONG).show();

            }
        });

        //calendario
        Calendar fecha = Calendar.getInstance();
        final int year = fecha.get(Calendar.YEAR);
        final int month = fecha.get(Calendar.MONTH);
        final int day = fecha.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateSelected = dayOfMonth+"/"+(month+1)+"/"+year;
            }
        },year,month,day);

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });




        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listacitasaux.clear();

                if(doctorSelected == 1 && especialidadSelected == 1 && dateSelected.equals("null")){
                    Toast.makeText(getContext(), "Debes agregar un filtro", Toast.LENGTH_LONG).show();
                }

                if(doctorSelected == 1 && especialidadSelected == 1 && !dateSelected.equals("null")){
                    Toast.makeText(getContext(), "Filtraste por fecha", Toast.LENGTH_LONG).show();


                    for(int i=0; i<listacitas.size(); i++){

                        Cita c = listacitas.get(i);
                        String n = c.getNombreDoc();
                        String n2 = listaDoctores.get(doctorSelected-1).getNombre();

                        String esp = c.getEspecialidad();
                        String esp2 = listaEspecialidad.get(especialidadSelected-1);

                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
                        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
                        String fechaString = sdf.format(c.getFechaCita());

                        System.out.println("fechas: "+fechaString+" mi fecha: "+dateSelected );
                        //n.equals(n2) && esp.equals(esp2) &&
                        if( fechaString.equals(dateSelected)){
                            listacitasaux.add(listacitas.get(i));
                        }

                    }
                    adapterCitaMedico.setLista(listacitasaux);
                    adapterCitaMedico.notifyDataSetChanged();

                }
                if(doctorSelected == 1 && especialidadSelected != 1 && dateSelected.equals("null")){
                    Toast.makeText(getContext(), "Filtraste por especialida ", Toast.LENGTH_LONG).show();

                    for(int i=0; i<listacitas.size(); i++){

                        Cita c = listacitas.get(i);

                        String esp = c.getEspecialidad();
                        String esp2 = listaEspecialidad.get(especialidadSelected-2);

                        System.out.println("esp: "+esp +" esp2: "+esp2);

                        if( esp.equals(esp2) ){
                            listacitasaux.add(listacitas.get(i));
                        }

                    }
                    adapterCitaMedico.setLista(listacitasaux);
                    adapterCitaMedico.notifyDataSetChanged();
                }
                if(doctorSelected == 1 && especialidadSelected != 1 && !dateSelected.equals("null")){
                    Toast.makeText(getContext(), "Filtraste por especialida y fecha", Toast.LENGTH_LONG).show();

                    //esp y fecha
                    for(int i=0; i<listacitas.size(); i++){

                        Cita c = listacitas.get(i);

                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
                        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
                        String fechaString = sdf.format(c.getFechaCita());

                        String esp = c.getEspecialidad();
                        String esp2 = listaEspecialidad.get(especialidadSelected-2);

                        System.out.println("esp: "+esp +" date: "+dateSelected);

                        if( esp.equals(esp2) && fechaString.equals(dateSelected) ){
                            listacitasaux.add(listacitas.get(i));
                        }

                    }
                    adapterCitaMedico.setLista(listacitasaux);
                    adapterCitaMedico.notifyDataSetChanged();

                    
                }
                if(doctorSelected != 1 && especialidadSelected == 1 && dateSelected.equals("null")){
                    Toast.makeText(getContext(), "Filtraste por doctor", Toast.LENGTH_LONG).show();

                    for(int i=0; i<listacitas.size(); i++){

                        Cita c = listacitas.get(i);

                        String esp = c.getNombreDoc();
                        String esp2 = listaDoctores.get(doctorSelected-2).getNombre();

                        System.out.println("esp: "+esp +" esp2: "+esp2);

                        if( esp.equals(esp2) ){
                            listacitasaux.add(listacitas.get(i));
                        }

                    }
                    adapterCitaMedico.setLista(listacitasaux);
                    adapterCitaMedico.notifyDataSetChanged();



                }
                if(doctorSelected != 1 && especialidadSelected == 1 && !dateSelected.equals("null")){
                    Toast.makeText(getContext(), "filtraste por doctor y fecha", Toast.LENGTH_LONG).show();

                    for(int i=0; i<listacitas.size(); i++){

                        Cita c = listacitas.get(i);

                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
                        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
                        String fechaString = sdf.format(c.getFechaCita());

                        String esp = c.getNombreDoc();
                        String esp2 = listaDoctores.get(doctorSelected-2).getNombre();

                        System.out.println("esp: "+esp +" date: "+dateSelected);

                        if( esp.equals(esp2) && fechaString.equals(dateSelected) ){
                            listacitasaux.add(listacitas.get(i));
                        }

                    }
                    adapterCitaMedico.setLista(listacitasaux);
                    adapterCitaMedico.notifyDataSetChanged();


                }
                if(doctorSelected != 1 && especialidadSelected != 1 && dateSelected.equals("null")){
                    Toast.makeText(getContext(), "filtraste por doctor y especialidad", Toast.LENGTH_LONG).show();

                    for(int i=0; i<listacitas.size(); i++){

                        Cita c = listacitas.get(i);
                        String n = c.getNombreDoc();
                        String n2 = listaDoctores.get(doctorSelected-2).getNombre();

                        String esp = c.getEspecialidad();
                        String esp2 = listaEspecialidad.get(especialidadSelected-2);

                        System.out.println("esp selected: "+esp2 +" doctor selected: "+n2);

                        if(n.equals(n2) && esp.equals(esp2) ){
                            listacitasaux.add(listacitas.get(i));
                        }

                    }
                    adapterCitaMedico.setLista(listacitasaux);
                    adapterCitaMedico.notifyDataSetChanged();
                }

                if(doctorSelected != 1 && especialidadSelected != 1 && !dateSelected.equals("null")){
                    Toast.makeText(getContext(), "usaste todos los filtros", Toast.LENGTH_LONG).show();

                    for(int i=0; i<listacitas.size(); i++){

                        Cita c = listacitas.get(i);

                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
                        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
                        String fechaString = sdf.format(c.getFechaCita());

                        String espx = c.getEspecialidad();
                        String espx2 = listaEspecialidad.get(especialidadSelected-2);

                        String esp = c.getNombreDoc();
                        String esp2 = listaDoctores.get(doctorSelected-2).getNombre();

                        System.out.println("esp selected: "+espx2 +" doctor selected: "+esp2 + " fecha selected: "+dateSelected+" fecha: "+fechaString );

                        if( esp.equals(esp2) && fechaString.equals(dateSelected) && espx.equals(espx2) ){
                            listacitasaux.add(listacitas.get(i));
                        }

                    }
                    adapterCitaMedico.setLista(listacitasaux);
                    adapterCitaMedico.notifyDataSetChanged();
                }

            }
        });


        return view;
    }//fin------------------------------


    public void crearPDF() {
        Document documento = new Document();

        try {
            File file = crearFichero(nombredocumento);
            FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());

            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPDF);

            documento.open();

            //iniciamos el calendario
            Calendar fecha = Calendar.getInstance();
            final int year = fecha.get(Calendar.YEAR);
            final int month = fecha.get(Calendar.MONTH);
            final int day = fecha.get(Calendar.DAY_OF_MONTH);

            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add("HOSPITAL XXXYYY \n\n");
            documento.add(new Paragraph("\n\n"));

            documento.add(paragraph);
            documento.add(new Paragraph("HISTORIA CLINICA \n\n"));
            documento.add(new Paragraph("BOGOTA DC. "+year+"/"+(month+1)+"/"+day+"\n\n"));
            documento.add(new Paragraph("PACIENTE: "+ paciente.getNombre()+"\n\n"));
            documento.add(new Paragraph("CORREO: "+ paciente.getCorreo()+"\n\n"));
            documento.add(new Paragraph("\n\n"));


            // Insertamos una tabla

            for(int i = 0 ; i < listacitas.size() ; i++) {
                PdfPTable tabla = new PdfPTable(1);

                DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                String fechaString = dateFormat.format(listacitas.get(i).getFechaCita());

                String[] parts2 = fechaString.split(" ");

                String fecha2 = parts2[0];
                fecha2 = fecha2 + " "+parts2[1]+" "+parts2[2]+" "+parts2[3];
                String hora = parts2[4];


                String cita = "Nombre Doctor: "+listacitas.get(i).getNombreDoc()+"\n\n"+
                              "Especialidda:  "+listacitas.get(i).getEspecialidad()+"\n\n"+
                              "Fecha: "+fecha2+"\n\n"+
                              "Hora:  "+hora+"\n\n"+
                              "Informe cita:  "+"\n\n"+
                              ""+listacitas.get(i).getDescripcion()+"\n\n";

                tabla.addCell(cita);
                documento.add(tabla);
                documento.add(new Paragraph("\n\n"));
            }

        } catch(DocumentException e) {
        } catch(IOException e) {
        } finally {
            documento.close();
        }
    }

    public File crearFichero(String nombreFichero) {
        File ruta = getRuta();

        File fichero = null;
        if(ruta != null) {
            fichero = new File(ruta, nombreFichero);
        }

        return fichero;
    }

    public File getRuta() {
        File ruta = null;

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), directorio);

            if(ruta != null) {
                if(!ruta.mkdirs()) {
                    if(!ruta.exists()) {
                        return null;
                    }
                }
            }

        }
        return ruta;
    }






}
