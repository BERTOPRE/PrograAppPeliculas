package com.example.moviesapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuscarActivity extends AppCompatActivity {

    EditText txtCodigoBuscar;
    TextView txtResultado;
    Button btnBuscar, btnEliminar;

    private static final String TAG = "BuscarActivity"; // 🔹 Tag para Logcat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        txtCodigoBuscar = findViewById(R.id.txtCodigoBuscar);
        txtResultado = findViewById(R.id.txtResultado);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Permitir conexión HTTP en el hilo principal (solo para desarrollo)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        btnBuscar.setOnClickListener(v -> buscarPelicula());
        btnEliminar.setOnClickListener(v -> eliminarPelicula());
    }

    // ===========================================================
    // 🔹 Método para buscar película por código (GET)
    // ===========================================================
    private void buscarPelicula() {
        try {
            String codigo = txtCodigoBuscar.getText().toString().trim();
            if (codigo.isEmpty()) {
                Toast.makeText(this, "Ingrese un código", Toast.LENGTH_SHORT).show();
                return;
            }

            // URL del web service (usa 10.0.2.2 si estás en emulador)
            URL url = new URL("http://10.0.2.2/api_inventario/?codigo=" + codigo);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String jsonResponse = sb.toString();
                Log.d(TAG, "____Respuesta JSON cruda: " + jsonResponse); // ✅ Imprime en Logcat

                JSONObject json = new JSONObject(jsonResponse);
                if (json.getBoolean("success")) {
                    JSONObject data = json.getJSONObject("data");

                    String info = "🎬 Nombre: " + data.getString("nombre_articulo") +
                            "\n📦 Existencias: " + data.getInt("existencias") +
                            "\n💰 Precio costo: Q" + data.getDouble("precio_costo") +
                            "\n💵 Precio venta: Q" + data.getDouble("precio_venta") +
                            "\n📚 Categoría: " + data.getString("categoria");

                    txtResultado.setText(info);

                    // 🔹 Imprimir los valores individuales también
                    Log.d(TAG, "Película encontrada:");
                    Log.d(TAG, "Nombre: " + data.getString("nombre_articulo"));
                    Log.d(TAG, "Categoría: " + data.getString("categoria"));
                    Log.d(TAG, "Precio venta: " + data.getDouble("precio_venta"));
                } else {
                    txtResultado.setText("No se encontró la película");
                    Log.d(TAG, " No se encontró la película con código: " + codigo);
                }
            } else {
                txtResultado.setText("Error en conexión: " + responseCode);
                Log.e(TAG, " Error HTTP: " + responseCode);
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            txtResultado.setText("Error: " + e.getMessage());
            Log.e(TAG, " Excepción al buscar película: " + e.getMessage(), e);
        }
    }

    // ===========================================================
    // 🔹 Método para eliminar película (DELETE)
    // ===========================================================
    private void eliminarPelicula() {
        try {
            String codigo = txtCodigoBuscar.getText().toString().trim();
            if (codigo.isEmpty()) {
                Toast.makeText(this, "Ingrese un código", Toast.LENGTH_SHORT).show();
                return;
            }

            URL url = new URL("http://10.0.2.2/api_inventario/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String jsonInput = "{\"codigo\": " + codigo + "}";
            conn.getOutputStream().write(jsonInput.getBytes("UTF-8"));
            conn.getOutputStream().close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "🗑️ Código respuesta DELETE: " + responseCode);

            if (responseCode == 200) {
                txtResultado.setText("Película eliminada correctamente");
            } else {
                txtResultado.setText("Error al eliminar: " + responseCode);
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            txtResultado.setText("Error: " + e.getMessage());
            Log.e(TAG, " Excepción al eliminar película: " + e.getMessage(), e);
        }
    }
}
