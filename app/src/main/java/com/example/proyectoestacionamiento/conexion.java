package com.example.proyectoestacionamiento;


import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class conexion {

    JSONObject request_data;
    JSONObject result_data;
    RequestQueue queue;

    //recibe el tipo de metodo (get,post,put), la url de conexion
    public conexion(Context contexto) {
        request_data = new JSONObject();
        result_data = new JSONObject();
        queue = Volley.newRequestQueue(contexto);
    }


    public JSONObject registro_usuario(int metodo,String url,ArrayList<String> params) {
        try {
            request_data.put("user", params.get(0));
            request_data.put("password", params.get(1));
            request_data.put("nombre", params.get(2));
            request_data.put("apellido", params.get(3));
            request_data.put("correo", params.get(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realizar_conexion(metodo,url);
        return  result_data;
    }


    public JSONObject Conexion_login(int metodo,String url,ArrayList<String> params) {
        try {
            request_data.put("usuario", params.get(0));
            request_data.put("password", params.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realizar_conexion(metodo,url);
        return result_data;
    }

    public JSONObject conexion_qr(int metodo, String url, ArrayList<String> params){
        try{
            request_data.put("id_parquimetro",params.get(0));
            request_data.put("usuario",params.get(1));
        }catch (JSONException exp){
            exp.printStackTrace();
        }
        realizar_conexion(metodo,url);
        return result_data;
    }



    private void realizar_conexion(int metodo,String url) {
        JsonObjectRequest jsonObjectRequest = null;
        if (metodo == Request.Method.GET) {
            Log.d("entro al metodo","entro al metodo realizar_conexion");
            // Request a string response from the provided URL.
              jsonObjectRequest = new JsonObjectRequest
                    (metodo, url, null, new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            // Display the first 500 characters of the response string.
                            //result_data=response;


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                     Log.d("Error",error.toString());
                }
            });
        } else if (metodo == Request.Method.POST) {
            jsonObjectRequest = new JsonObjectRequest
                    (metodo, url, request_data, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                result_data.put("estado",response.getString("estado"));
                                result_data.put("flag",response.getString("flag"));
                            } catch (JSONException e) {
                                e.printStackTrace();
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
        }
        queue.add(jsonObjectRequest);
    }

}