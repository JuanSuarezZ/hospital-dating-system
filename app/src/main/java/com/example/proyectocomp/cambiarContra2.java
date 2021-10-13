package com.example.proyectocomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class cambiarContra2 extends AppCompatActivity {

    String id;
    EditText txtpass1,txtpass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contra2);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");

        txtpass1 = (EditText) findViewById(R.id.txtcontrap1);
        txtpass2 = (EditText) findViewById(R.id.txtcontraP2);

    }

    public void cambiar(View view){

        if (txtpass1.getText().toString().isEmpty()) {
            txtpass1.setError("DEBE RELLENAR ESTE CAMPO");
        } else if (txtpass2.getText().toString().isEmpty()) {
            txtpass2.setError("DEBE RELLENAR ESTE CAMPO");
        } else{

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("contra", txtpass1.getText());
                jsonObject.put("idx", id.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient okHttpClient = new OkHttpClient();
            //aca hago el json
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            okhttp3.Request request = new okhttp3.Request.Builder().url("https://juansuarezz-servidorflask.zeet.app/CambiarP").post(body).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    volverMain();
                }
            });
        }
        ///fin enviar Json
    }

    public void volverMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancelar(View view){
        Intent intent = new Intent(this, validarCodigo2.class);
        startActivity(intent);
    }


}