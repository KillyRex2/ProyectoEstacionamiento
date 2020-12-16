package com.example.proyectoestacionamiento;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Tiempo extends AppCompatActivity {

    //atributos utilizados para realizar la conexion
    JSONObject request_data;
    JSONObject result_data;
    RequestQueue queue;

    private TextView TextViewTiempo;
    private Button b1;
    boolean estado;
    int Segundos, Minutos, Horas ;
    String  parquimetro,costoS,usuario,fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiempo);

        usuario = getIntent().getStringExtra("usuario");
        parquimetro=getIntent().getStringExtra("parquimetro");


        TextViewTiempo = (TextView) findViewById(R.id.TextViewT);
        Segundos = Minutos = Horas = 0;
        estado=false;
        b1 = (Button) findViewById(R.id.ButtonFinalizar);
        new Thread(new Runnable() {
            //int Segundos = 0, Minutos = 0, Horas = 0;
            @Override
            public void run() {
                while (true) {
                    if (Segundos / 60 > 0) {
                        Minutos = Minutos + (Segundos / 60);
                        Segundos = 0;
                    }
                    if (Minutos / 60 > 0) {
                        Horas = Horas + (Minutos / 60);
                        Minutos = 0;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    if(estado)
                        break;
                    Segundos++;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextViewTiempo.setText(String.valueOf(Horas) + ":" + String.valueOf(Minutos) + ":" + String.valueOf(Segundos));
                        }
                    });
                }
            }
        }).start();


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado=true;
                //mostrar ventana sobre el costos
                double costo = 0;
                final double precioSegundos=.002777777;
                double  totalSegundos;
                totalSegundos=Segundos+(Minutos*60)+(Horas*3600);
                costo=totalSegundos*precioSegundos;
                final Intent intent = new Intent(Tiempo.this,VentanaDeCostos.class);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                Date date = new Date();

               fecha = dateFormat.format(date);


               //enviar datos A la BD
                queue = Volley.newRequestQueue(Tiempo.this);
                String url="http://maniacorp.ddns.net:3000/realizar_cobro";

                //instanciar la cola de peticiones HTTP
                request_data = new JSONObject();
                result_data = new JSONObject();
                queue = Volley.newRequestQueue(Tiempo.this);

                //crear el objeto json que vamos a mandar a la bd
                try {
                    request_data.put("usuario", usuario);
                    request_data.put("id_parquimetro", parquimetro);
                    request_data.put("costo", costo);
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
                           if(response.getString("flag").equals("flase")){
                               Toast.makeText(Tiempo.this,response.getString("estado"),Toast.LENGTH_LONG).show();
                            }else{
                                 Toast.makeText(Tiempo.this,response.getString("estado"),Toast.LENGTH_LONG).show();
                                 //mostrar alertdialgo
                                 AlertDialog.Builder alerta = new AlertDialog.Builder(Tiempo.this);
                                 alerta.setMessage("Gracias por Usar Nuestro Servicio\n" +
                                 "Tiempo Transcurrido: "+String.valueOf(Horas)+":"+String.valueOf(Minutos)+":"+String.valueOf(Segundos)+"\n"+
                                 "Total a Pagar: "+String.valueOf(costoS))
                                  .setCancelable(false)
                                  .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {


                                           }
                                       })
                                       .setNegativeButton("Estacionarse Nuevamente", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {

                                           }
                                       }).show();
                            }
                        } catch (JSONException e) {
                             Log.d("error",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("error", error.toString());
                    }
                });
                queue.add(jsonObjectRequest);
            }
        });
    }
}
