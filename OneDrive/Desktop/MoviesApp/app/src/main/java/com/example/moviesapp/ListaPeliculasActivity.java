package com.example.moviesapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListaPeliculasActivity extends AppCompatActivity {

    RecyclerView recyclerPeliculas;
    ArrayList<Pelicula> listaPeliculas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_peliculas);

        recyclerPeliculas = findViewById(R.id.recyclerPeliculas);
        recyclerPeliculas.setLayoutManager(new LinearLayoutManager(this));

        // Permitir conexión de red (solo para pruebas)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        obtenerPeliculas();
    }

    private void obtenerPeliculas() {
        try {
            URL url = new URL("http://10.0.2.2/api_inventario/"); // si usas emulador
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject json = new JSONObject(sb.toString());
                if (json.getBoolean("success")) {
                    JSONArray data = json.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        Pelicula p = new Pelicula(
                                obj.getInt("codigo"),
                                obj.getString("nombre_articulo"),
                                obj.getInt("existencias"),
                                obj.getDouble("precio_costo"),
                                obj.getDouble("precio_venta"),
                                obj.getString("categoria")
                        );
                        listaPeliculas.add(p);
                    }
                    recyclerPeliculas.setAdapter(new PeliculasAdapter(listaPeliculas));
                } else {
                    Toast.makeText(this, "No hay películas", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
