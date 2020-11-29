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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private Button sesion,registro, prueba;
    private EditText email,pass;
    String correo,contraseña;
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

                correo=email.getText().toString();
                contraseña=pass.getText().toString();
                //enviar datos A la BD
                RequestQueue queue = Volley.newRequestQueue(login.this);
                String url="http://www.parquimetro.somee.com/Inicio_Sesion.php";
                StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                if(!response.isEmpty()){

                                    for (int i =0;i<response.length();i++)
                                        if(response.charAt(i)=='<') {
                                            response = response.substring(0, i);
                                            break;
                                        }

                                    String[] arrOfStr = response.split(",", 3);
                                    Log.d("Response", arrOfStr.toString());
                                    Intent intent = new Intent(login.this, Inicio.class);
                                    intent.putExtra("id", arrOfStr[0]);
                                    intent.putExtra("Nombre", arrOfStr[1]);
                                    intent.putExtra("Apellido", arrOfStr[2]);
                                    startActivity(intent);
                                }else{
                                    Log.d("Response", "entro al else");
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                                    alerta.setMessage("Correo o Contraseña incorrecta Verifica los Datos")
                                            .setCancelable(false)
                                            .setPositiveButton("Verificar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", "Error al Enviar los Datos");
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Correo",correo);
                        params.put("Password",contraseña);
                        return params;
                    }

                };
                queue.add(putRequest);
                //--------------------------------------------


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
