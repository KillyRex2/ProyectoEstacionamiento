package com.example.proyectoestacionamiento;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private Button sesion,registro;
    private EditText email,pass;
    String correo,contraseña;
    URL url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //codigo para salir de la app
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        sesion=(Button) findViewById(R.id.Sesion);
        registro=(Button) findViewById(R.id.btRegistro);
        email=(EditText) findViewById(R.id.editTextCorreo);
        pass=(EditText) findViewById(R.id.editTextContraseña);

        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = email.getText().toString();
                contraseña = pass.getText().toString();
                 conexion con = new conexion();
                 String url = "192.168.1.50:3000/login";
                 con.execute(url);
                 String dato = con.doInBackground(url,"login",correo,contraseña);
                 Log.i("Datos",dato);
                 Log.i("Dato","login");

                 Intent intent = new Intent(login.this, Inicio.class);
                 startActivity(intent);
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, Sing_Up.class);
                startActivity(intent);
            }
        });

    }
}
