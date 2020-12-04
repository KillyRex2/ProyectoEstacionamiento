package com.example.proyectoestacionamiento;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class conexion extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String...strings) {
        // 0:url 1:tipo  2 --> datos
        JSONObject jsonObject = null;
        URL url = null;
        String datos = "";
        try{
           url = new URL(strings[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(strings[1].equals("login")) {
            //Crear Objeto JSON
            try {
                jsonObject = new JSONObject();
                jsonObject.put("usuario", strings[2]);
                jsonObject.put("password", strings[3]);
            } catch (JSONException e) {
                Log.e("Error", "Failed to Create JSONObject", e);
            }
            datos = postConnection(url);
        }else if (strings[1].equals("registro")){
            //Crear Objeto JSON
            try {
                jsonObject = new JSONObject();
                jsonObject.put("user", strings[2]);
                jsonObject.put("password", strings[3]);
                jsonObject.put("nombre", strings[4]);
                jsonObject.put("apellido", strings[5]);
                jsonObject.put("correo", strings[5]);
            } catch (JSONException e) {
                Log.e("Error", "Failed to Create JSONObject", e);
            }
           datos = postConnection(url);
        }
          return datos;
    }

    public String postConnection(URL url){
        StringBuffer buffer = null;
        //crear Peticion Post
        try {
            HttpURLConnection httpPost = (HttpURLConnection) url.openConnection();
            httpPost.setRequestMethod("POST");
            httpPost.setRequestProperty("Content-Type", "application/json; utf-8");
            httpPost.setRequestProperty("Accept", "application/json");
            httpPost.setDoOutput(true);
            httpPost.setDoInput(true);
            httpPost.connect();
            int code = httpPost.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(httpPost.getInputStream());
                BufferedReader bf = new BufferedReader(new InputStreamReader(in));
                String linea = "";
                buffer = new StringBuffer();
                while ((linea = bf.readLine()) != null) {
                    buffer.append(linea);
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

}
