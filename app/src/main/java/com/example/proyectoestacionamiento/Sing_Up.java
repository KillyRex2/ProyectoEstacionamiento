package com.example.proyectoestacionamiento;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.Arrays;


public class Sing_Up extends AppCompatActivity {

    //atributos utilizados para realizar la conexion
    JSONObject request_data;
    JSONObject result_data;
    RequestQueue queue;

    private EditText  Nombre,Apellido,Correo,Contraseña,repetirContraseña,Usuario;
    private Button  botonContinuar;
    private CheckBox TerminosYCondiciones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing__up);


        Nombre=(EditText) findViewById(R.id.editTextNombre);
        Apellido=(EditText) findViewById(R.id.editTextApellido);
        Correo=(EditText) findViewById(R.id.editTextCorreo);
        Contraseña=(EditText) findViewById(R.id.editTextContraseña);
        repetirContraseña=(EditText) findViewById(R.id.editTextRepetirContraseña);
        TerminosYCondiciones=(CheckBox) findViewById(R.id.checkBoxTerminos);
        botonContinuar=(Button) findViewById(R.id.btContinuar);
        Usuario = (EditText) findViewById(R.id.edtUsuario);

        TerminosYCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(TerminosYCondiciones.isChecked())
                    terminosClick();
            }
        });



        //metodo listener para el boton
        botonContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject resultado = null;
                    if(TerminosYCondiciones.isChecked()) {
                        if(Usuario.getText().toString().trim().isEmpty()){
                            Toast.makeText(Sing_Up.this, "Por Favor Ingresé un Usuario", Toast.LENGTH_LONG).show();
                        }
                        else if(Nombre.getText().toString().trim().isEmpty()) {
                            Toast.makeText(Sing_Up.this, "Por Favor Ingresé su Nombre", Toast.LENGTH_LONG).show();
                        }else if (Apellido.getText().toString().trim().isEmpty()){
                                Toast.makeText(Sing_Up.this,"Por Favor Ingresé su Apellido", Toast.LENGTH_LONG).show();
                        }else if (Correo.getText().toString().trim().isEmpty()){
                            Toast.makeText(Sing_Up.this,"Por Favor Ingresé un Correo", Toast.LENGTH_LONG).show();
                        }else if (Contraseña.getText().toString().trim().isEmpty()){
                            Toast.makeText(Sing_Up.this,"Por Favor Ingresé una Contraseña", Toast.LENGTH_LONG).show();
                        }else if (!repetirContraseña.getText().toString().trim().equals(Contraseña.getText().toString().trim())) {
                            Toast.makeText(Sing_Up.this,"La contraseña no coincide", Toast.LENGTH_LONG).show();
                        }else{
                                realizarConexion();
                        }
                    }else{
                        Toast.makeText(Sing_Up.this,"Por Favor Aceptar Terminos y Condiciones", Toast.LENGTH_LONG).show();
                    }

                }
            }
        );

    }








    public void terminosClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terminos y condiciones de Maniacorp: ")
                .setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer vel dolor id urna dapibus cursus in id odio. Aenean eget odio id purus tincidunt volutpat quis at quam. Fusce ullamcorper mattis elit. Pellentesque mauris risus, fermentum ac felis et, tincidunt lacinia tellus. Nullam varius lorem in rhoncus congue. Curabitur vel ante aliquam ligula sodales iaculis. Fusce fringilla dapibus sapien, vel suscipit massa auctor sed. Aliquam iaculis lobortis placerat. Etiam bibendum leo eu turpis fringilla, ac consequat lorem cursus. Ut at mauris dolor. Praesent sagittis nulla ut sapien elementum porta. Nam non auctor urna, id pharetra odio. Aenean ultricies lorem urna, vitae faucibus dolor consequat vel. Quisque iaculis mattis ex, eget convallis lorem mattis venenatis. Etiam elementum dapibus ligula, ut scelerisque orci tempus sit amet.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }


    public void btnPaypalClick(View v){
        Intent intent = new Intent(this, PaypalActivity.class);
        startActivity(intent);
    }

    private void llamar_Activity(){
        Intent intent = new Intent(Sing_Up.this,Inicio.class);

        intent.putExtra("nombre", Nombre.getText().toString());
        intent.putExtra("apellido", Apellido.getText().toString());
        intent.putExtra("usuario",Usuario.getText().toString());

        startActivity(intent);
    }



    private void realizarConexion(){
        //indicar la direccion para obtener datos de la bd
        String url = "http://192.168.1.50:3000/usuarios";

        //instanciar la cola de peticiones HTTP
        request_data = new JSONObject();
        result_data = new JSONObject();
        queue = Volley.newRequestQueue(this);

        //crear el objeto json que vamos a mandar a la bd
        try {
            request_data.put("user",Usuario.getText().toString());
            request_data.put("nombre",Nombre.getText().toString());
            request_data.put("apellido",Apellido.getText().toString());
            request_data.put("correo",Correo.getText().toString());
            request_data.put("password",Contraseña.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //realizar la conexion con la bd
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request_data, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //pasar los datos a un jsonObject para el acceso global dentro del activity
                try {
                    result_data.put("estado", response.getString("estado"));
                    result_data.put("flag", response.getString("flag"));
                    if(response.getString("flag").equals("false")){
                        Toast.makeText(Sing_Up.this,response.getString("estado"),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Sing_Up.this,response.getString("estado"),Toast.LENGTH_LONG).show();
                        llamar_Activity();
                    }
                } catch (JSONException e) {
                    try {
                        Toast.makeText(Sing_Up.this,response.getString("estado"),Toast.LENGTH_LONG).show();
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

}
