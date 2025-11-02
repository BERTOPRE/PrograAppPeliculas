package com.example.moviesapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AgregarActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText txtCodigo, txtNombre, txtExistencias, txtCosto, txtVenta, txtCategoria;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        db = new DatabaseHelper(this);

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombre);
        txtExistencias = findViewById(R.id.txtExistencias);
        txtCosto = findViewById(R.id.txtCosto);
        txtVenta = findViewById(R.id.txtVenta);
        txtCategoria = findViewById(R.id.txtCategoria);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Permitir llamadas HTTP directas (solo para pruebas locales)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnGuardar.setOnClickListener(v -> {
            try {
                int codigo = Integer.parseInt(txtCodigo.getText().toString());
                String nombre = txtNombre.getText().toString();
                int existencias = Integer.parseInt(txtExistencias.getText().toString());
                double costo = Double.parseDouble(txtCosto.getText().toString());
                double venta = Double.parseDouble(txtVenta.getText().toString());
                String categoria = txtCategoria.getText().toString();

                // Primero guardamos en la base de datos local (SQLite)
                boolean ok = db.insertarArticulo(codigo, nombre, existencias, costo, venta, categoria);

                //  Luego enviamos al servidor PHP (MySQL)
                enviarDatosServidor(codigo, nombre, existencias, costo, venta, categoria);

                Toast.makeText(this, ok ? "🎬 Película guardada y enviada al servidor" : "⚠️ Error al guardar localmente", Toast.LENGTH_SHORT).show();

                // Limpia los campos
                txtCodigo.setText("");
                txtNombre.setText("");
                txtExistencias.setText("");
                txtCosto.setText("");
                txtVenta.setText("");
                txtCategoria.setText("");

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    // 🔹 Método para enviar datos al servidor con POST (PHP)
    private void enviarDatosServidor(int codigo, String nombre, int existencias,
                                     double costo, double venta, String categoria) {

        try {
            // URL de tu API PHP (ajústala según tu caso)
            URL url = new URL("http://10.0.2.2/api_inventario/"); // Emulador → localhost
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // Crear JSON para enviar
            JSONObject json = new JSONObject();
            json.put("codigo", codigo);
            json.put("nombre_articulo", nombre);
            json.put("existencias", existencias);
            json.put("precio_costo", costo);
            json.put("precio_venta", venta);
            json.put("categoria", categoria);

            // Enviar datos al servidor
            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // Todo OK
                System.out.println(" Enviado correctamente al servidor");
            } else {
                System.out.println("Error en servidor: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al conectar con el servidor: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
