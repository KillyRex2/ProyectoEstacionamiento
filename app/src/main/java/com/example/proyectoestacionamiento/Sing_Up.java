package com.example.proyectoestacionamiento;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public class Sing_Up extends AppCompatActivity {



    private EditText  Nombre,Apellido,Correo,Contraseña,repetirContraseña;
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



        //metodo listener para el boton
        botonContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(TerminosYCondiciones.isChecked()) {

                        if(Nombre.getText().toString().isEmpty()) {
                            Toast.makeText(Sing_Up.this, "Por Favor Ingresé su Nombre", Toast.LENGTH_LONG).show();
                        }else if (Apellido.getText().toString().isEmpty()){
                                Toast.makeText(Sing_Up.this,"Por Favor Ingresé su Apellido", Toast.LENGTH_LONG).show();
                        }else if (Correo.getText().toString().isEmpty()){
                            Toast.makeText(Sing_Up.this,"Por Favor Ingresé un Correo", Toast.LENGTH_LONG).show();
                        }else if (Contraseña.getText().toString().isEmpty()){
                            Toast.makeText(Sing_Up.this,"Por Favor Ingresé una Contraseña", Toast.LENGTH_LONG).show();
                        }else if (repetirContraseña.getText().toString().equals(Contraseña.getText().toString())){
                                  Bundle Datos = new Bundle();
                                  Datos.putString("Nombre",Nombre.getText().toString());
                                  Datos.putString("Apellido",Apellido.getText().toString());
                                  Datos.putString("Correo",Correo.getText().toString());
                                  Datos.putString("Contraseña",Contraseña.getText().toString());
                                  //FormadePagoActivity(Datos);
                        }else {
                            Toast.makeText(Sing_Up.this,"La Contraseña no coincide ", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Sing_Up.this,"Por Favor Aceptar Terminos y Condiciones", Toast.LENGTH_LONG).show();
                    }

                }
            }
        );

    }

    public void ternimosClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Android")
                .setMessage("Dialogo basico con boton OK")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Sing_Up.this,"Aceptar ", Toast.LENGTH_LONG).show();
                    }
                })
                .create()
                .show();
    }
//    public void FormadePagoActivity (Bundle Datos){
//        Intent i = new Intent(this, Pago.class);
//        i.putExtras(Datos);
//        startActivity(i);
//    }

    public void btnPaypalClick(View v){
        Intent intent = new Intent(this, PaypalActivity.class);
        startActivity(intent);

    }

}
