package edu.continental.ciudades;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	ListView listaciudades;
	Button btnActualizar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listaciudades = (ListView) findViewById(R.id.listaciudades);
        
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        
        TareaWSListar tarea = new TareaWSListar();
        tarea.execute();
        
        
        btnActualizar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        TareaWSListar tarea = new TareaWSListar();
		        tarea.execute();			
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    private class TareaWSListar extends AsyncTask<String, Integer, Boolean>{

    	private ProgressDialog cargando = new ProgressDialog(MainActivity.this);
    	private String[] ciudades;
    	
    	
		@Override
		protected void onPreExecute() {
			cargando.setMessage("Cargando..");
			cargando.show();
		}



		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			boolean resultado = true;
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet llamada = new HttpGet("http://172.16.82.17:8080/restfull/rest/primerws/listaciudades/");
			llamada.setHeader("content-type", "application/json");
			
			try {
				HttpResponse respuesta = httpClient.execute(llamada);
				String cadena = EntityUtils.toString(respuesta.getEntity());
				
				JSONArray respuestaJSON = new JSONArray(cadena);
				
				ciudades = new String[respuestaJSON.length()];
				
				for (int i = 0; i < respuestaJSON.length(); i++) {
					JSONObject objeto = respuestaJSON.getJSONObject(i);
					
					
					String idciudad = objeto.getString("id");
					String nombre = objeto.getString("nombre");
					String altitud = objeto.getString("altitud");
					
					ciudades[i] = idciudad + "-" + nombre + "-" + altitud;
				}
				
				
				
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error", ex);
				resultado = false;
			}
			
			return resultado;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			cargando.dismiss();
			
			if (result ){
				
				ArrayAdapter<String> adaptador = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, ciudades);
				listaciudades.setAdapter(adaptador);
			}
			
		}		
    	
    	
    }
    
    
    
}
