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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;


public class Inicio extends AppCompatActivity {



    private TextView t1;
    private Button b1;
    String resultado,id_cliente;
    String Nombre,Apellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        t1=(TextView) findViewById(R.id.textViewNombre);
        b1=(Button) findViewById(R.id.bEscanear);
        Nombre=getIntent().getStringExtra("Nombre");
        Apellido=getIntent().getStringExtra("Apellido");
        id_cliente=getIntent().getStringExtra("id");
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
                resultado =result.getContents();
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
        RequestQueue queue = Volley.newRequestQueue(Inicio.this);
        String url="http://www.parquimetro.somee.com/EstadosParquimetro.php";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        response=response.substring(0,7);
                        if(response.equals("Ocupado")) {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(Inicio.this);
                            alerta.setMessage("El Parquimetro esta Ocupado")
                            .setCancelable(false)
                            .setPositiveButton("Intentar Nuevamente", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    scanCode();
                                }
                            }).show();
                        } else {
                            Intent intent = new Intent(Inicio.this, Tiempo.class);
                            intent.putExtra("id_parquimetro",resultado);
                            intent.putExtra("id",id_cliente);
                            intent.putExtra("Nombre", Nombre);
                            intent.putExtra("Apellido", Apellido);
                            startActivity(intent);
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
                params.put("id_lugar",resultado);
                return params;
            }

        };
        queue.add(putRequest);
        //--------------------------------------------
    }



}
