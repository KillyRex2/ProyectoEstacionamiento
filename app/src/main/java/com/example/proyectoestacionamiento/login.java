package com.example.proyectoestacionamiento;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class login extends AppCompatActivity {

    //atributos utilizados para realizar la conexion
    JSONObject request_data;
    JSONObject result_data;
    RequestQueue queue;

    //atributos de la clase
    private Button sesion,registro;
    private EditText email,pass;
    String usuario,contraseña;

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
                usuario = email.getText().toString();
                contraseña = pass.getText().toString();

                if(usuario.trim().isEmpty() || contraseña.trim().isEmpty()){
                    Toast.makeText(login.this,"Por favor ingresar un Usuario o una contraseña",Toast.LENGTH_LONG).show();
                }else{
                    realizarConexion();
                }
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

    public void  llamar_Activity(){
        Log.d("entro","entro");
        Intent intent = new Intent(login.this, Inicio.class);
        try {
            intent.putExtra("nombre", result_data.getString("nombre"));
            intent.putExtra("apellido", result_data.getString("apellido"));
            intent.putExtra("usuario",usuario);
        }catch (JSONException ex){
             Log.d("Error","parametros no encontrados");
        }
        startActivity(intent);
    }




    //metodo que permite realizar la conexion con la bd
    private void  realizarConexion(){

        //indicar la direccion para obtener datos de la bd
        String url = "http://maniacorp.ddns.net:3000/Login";

        //instanciar la cola de peticiones HTTP
        request_data = new JSONObject();
        result_data = new JSONObject();
        queue = Volley.newRequestQueue(this);

        //crear el objeto json que vamos a mandar a la bd
        try {
            request_data.put("usuario", usuario);
            request_data.put("password", contraseña);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //realizar la conexion con la bd
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request_data, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //pasar los datos a un jsonObject para el acceso global dentro del activity
                Log.d("datos",response.toString());
                try {
                    result_data.put("nombre", response.getString("nombre"));
                    result_data.put("apellido", response.getString("apellido"));
                    result_data.put("estado", response.getString("estado"));
                    Toast.makeText(login.this,response.getString("estado"),Toast.LENGTH_LONG).show();
                    llamar_Activity();
                } catch (JSONException e) {
                    try {
                        Toast.makeText(login.this,response.getString("estado"),Toast.LENGTH_LONG).show();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }
            }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                       Log.d("error", error.toString());
                  }
              });
        //agregar solicitud a la cola
        queue.add(jsonObjectRequest);
    }

}//cierre de clase


