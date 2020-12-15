package com.example.proyectoestacionamiento;

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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Inicio extends AppCompatActivity {

    //atributos utilizados para realizar la conexion
    JSONObject request_data;
    JSONObject result_data;
    RequestQueue queue;

    private TextView t1;
    private Button b1;
    String resultado,usuario;
    String Nombre,Apellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        t1=(TextView) findViewById(R.id.textViewNombre);
        b1=(Button) findViewById(R.id.bEscanear);
        Nombre=getIntent().getStringExtra("nombre");
        usuario = getIntent().getStringExtra("usuario");
        Apellido=getIntent().getStringExtra("apellido");
        t1.setText(Nombre+" "+Apellido);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
    }

    public  void closeSesionClick(View v){
        Intent intent = new Intent(Inicio.this, login.class );
    }

    public void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()!=null){
                resultado = result.getContents(); //obtenemos el id del parquimetro
                Tiempo();
            }else{
                Toast.makeText(this,"No Results",Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    public void Tiempo(){
        //enviar datos A la BD
        String url="http://maniacorp.ddns.net:3000/qr_estado";


        //instanciar la cola de peticiones HTTP
        request_data = new JSONObject();
        result_data = new JSONObject();
        queue = Volley.newRequestQueue(this);

        //crear el objeto json que vamos a mandar a la bd
        try {
            request_data.put("usuario", usuario);
            request_data.put("id_parquimetro", resultado);
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
                        //validar que el parquimetro este en uso
                        AlertDialog.Builder alerta = new AlertDialog.Builder(Inicio.this);
                        alerta.setMessage(response.getString("estado"))
                                .setCancelable(false)
                                .setPositiveButton("Intentar Nuevamente", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        scanCode();
                                    }
                                }).show();
                    }else{
                        Toast.makeText(Inicio.this,response.getString("estado"),Toast.LENGTH_LONG).show();
                        llamar_Activity();
                    }
                } catch (JSONException e) {
                    try {
                        Toast.makeText(Inicio.this,response.getString("estado"),Toast.LENGTH_LONG).show();
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

    private void llamar_Activity(){


    }


}
