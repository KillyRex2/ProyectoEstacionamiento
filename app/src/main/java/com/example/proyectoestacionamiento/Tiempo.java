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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Tiempo extends AppCompatActivity {


    private TextView TextViewTiempo;
    private Button b1;
    boolean estado;
    int Segundos, Minutos, Horas ;
    String idParquimetro,Nombre,Apellido;
    String costoS;
    String id_cliente;
    String fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiempo);

        id_cliente= getIntent().getStringExtra("id");
        idParquimetro=getIntent().getStringExtra("id_parquimetro");
        Nombre=getIntent().getStringExtra("Nombre");
        Apellido=getIntent().getStringExtra("Apellido");
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
                costoS=String.valueOf(costo);
                final Intent intent = new Intent(Tiempo.this,VentanaDeCostos.class);



                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                Date date = new Date();

               fecha = dateFormat.format(date);
                //enviar datos A la BD
                RequestQueue queue = Volley.newRequestQueue(Tiempo.this);
                String url="http://www.parquimetro.somee.com/Transferencia.php";
                StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
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
                        params.put("id_cliente",id_cliente);
                        params.put("id_lugar",idParquimetro);
                        params.put("importe",costoS);
                        params.put("fecha",fecha);
                        return params;
                    }

                };
                queue.add(putRequest);
                //--------------------------------------------

                AlertDialog.Builder alerta = new AlertDialog.Builder(Tiempo.this);
                alerta.setMessage("Gracias por Usar Nuestro Servicio\n" +
                                  "Tiempo Transcurrido: "+String.valueOf(Horas)+":"+String.valueOf(Minutos)+":"+String.valueOf(Segundos)+"\n"+
                                  "Total a Pagar: "+String.valueOf(costoS))
                        .setCancelable(false)
                        .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Tiempo.this, login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Estacionarse Nuevamente", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Tiempo.this, Inicio.class);
                                intent.putExtra("Nombre", Nombre);
                                intent.putExtra("Apellido", Apellido);
                                intent.putExtra("id", id_cliente);
                                startActivity(intent);
                            }
                        }).show();
            }

        });
    }
}
