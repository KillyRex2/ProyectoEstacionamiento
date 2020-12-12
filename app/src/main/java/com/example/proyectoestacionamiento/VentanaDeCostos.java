package com.example.proyectoestacionamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class VentanaDeCostos extends AppCompatActivity {

     private TextView TiempoTranscurrido,Tiempo,costo,precioPorHora;
     private Button reinicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_de_costos);

        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);
        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int)(ancho*.90),(int) (alto*0.7));

        TiempoTranscurrido=(TextView)findViewById(R.id.textvTiempoTranscurrido);
        Tiempo=(TextView)findViewById(R.id.textvTiempo);
        costo=(TextView) findViewById(R.id.textvTotal);
        precioPorHora=(TextView) findViewById(R.id.textvPrecioHora);
        Bundle datos= getIntent().getExtras();
        String Hora=datos.getString("Horas"),
               Minutos=datos.getString("Minutos"),
               Segundos=datos.getString("Segundos"),
               Costos=datos.getString("Costos");
        TiempoTranscurrido.setText(Hora+":"+Minutos+":"+Segundos);
        costo.setText(costo.getText()+" $"+Costos);
        reinicio=(Button) findViewById(R.id.btAgain);
        reinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VentanaDeCostos.this, Inicio.class);
                startActivity(intent);
            }
        });
    }
}
