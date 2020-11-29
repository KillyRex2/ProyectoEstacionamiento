package com.example.proyectoestacionamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Pago extends AppCompatActivity{

    private EditText ETNombreTitular,ETNumeroTarjeta,ETCodigoSeguridad,ETFechaEM,ETFechaEA;
    private Button Enviar;

    String Nombre,Apellido,Correo,Contraseña;
    String NombreTitular,NumeroTarjeta,CodigoSeguridad,FechaEM,FechaEA;
    String id_cliente;


    public interface DatosResponseListener
    {
        void datosResponse(String datos);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        //datos del activity anterior mandados
        Nombre = getIntent().getStringExtra("Nombre");
        Apellido=getIntent().getStringExtra("Apellido");
        Correo = getIntent().getStringExtra("Correo");
        Contraseña = getIntent().getStringExtra("Contraseña");


        //declarar items del activity
        ETNombreTitular=(EditText) findViewById(R.id.editTextNombre);
        ETNumeroTarjeta=(EditText) findViewById(R.id.editTextTarjeta);
        ETCodigoSeguridad=(EditText) findViewById(R.id.editTextNumero);
        ETFechaEM=(EditText) findViewById(R.id.FEM);
        ETFechaEA=(EditText) findViewById(R.id.FEA);
        Enviar=(Button) findViewById(R.id.btContinuar);




         Enviar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 //datos del activity actual
                 NombreTitular=ETNombreTitular.getText().toString();
                 NumeroTarjeta=ETNumeroTarjeta.getText().toString();
                 CodigoSeguridad=ETCodigoSeguridad.getText().toString();
                 FechaEM=ETFechaEM.getText().toString();
                 FechaEA=ETFechaEA.getText().toString();



                 if(NombreTitular.isEmpty())
                     Toast.makeText(Pago.this,"Ingrese el nombre del titular",Toast.LENGTH_LONG).show();
                 else if(NumeroTarjeta.isEmpty())
                     Toast.makeText(Pago.this,"Ingrese el numero de la tarjeta",Toast.LENGTH_LONG).show();
                 else if (CodigoSeguridad.isEmpty())
                     Toast.makeText(Pago.this,"Ingrese el codigo de seguridad",Toast.LENGTH_LONG).show();
                 else if (FechaEA.isEmpty() || FechaEM.isEmpty())
                     Toast.makeText(Pago.this,"Ingrese la fecha de expiracion",Toast.LENGTH_LONG).show();
                 else {
                     cargarDatos("est", new DatosResponseListener(){
                         @Override
                         public void datosResponse(String response)
                         {
                             // procesas la respuesta del servidor
                             id_cliente=response;
                             //llamar al siguiente Activity
                             Intent intent = new Intent(Pago.this, Inicio.class);
                             intent.putExtra("Nombre", Nombre);
                             intent.putExtra("Apellido", Apellido);
                             intent.putExtra("id", id_cliente);
                             startActivity(intent);
                         }
                     });

                 }
             }
         });

    }



    public void cargarDatos(final String est,final DatosResponseListener listener){
        //enviar Datos
        RequestQueue queue = Volley.newRequestQueue(this);
        String url="http://www.parquimetro.somee.com/Registro_Usuarios.php";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        for(int i=0;i<response.length();i++) {
                            if (Character.isDigit(response.charAt(i))) {
                                continue;
                            } else {
                                response=response.substring(0,i);
                                listener.datosResponse(response);
                                break;
                            }
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
                Map<String, String>  params = new HashMap<String, String> ();
                params.put("Nombre",Nombre);
                params.put("Apellido",Apellido);
                params.put("Correo",Correo);
                params.put("Password",Contraseña);
                params.put("NombreTitular",NombreTitular);
                params.put("NTarjeta",NumeroTarjeta);
                params.put("CodigoS",CodigoSeguridad);
                params.put("FechaM",FechaEM);
                params.put("FechaA",FechaEA);
                return params;
            }

        };
        queue.add(putRequest);
    }




    //---------------------------


    //----------



}//cierre de la clase



