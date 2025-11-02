package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAgregar, btnBuscar, btnListaPeliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnListaPeliculas = findViewById(R.id.btnListaPeliculas);

        btnAgregar.setOnClickListener(v -> startActivity(new Intent(this, AgregarActivity.class)));
        btnBuscar.setOnClickListener(v -> startActivity(new Intent(this, BuscarActivity.class)));

        // ✅ Nuevo botón que abre la lista completa
        btnListaPeliculas.setOnClickListener(v ->
                startActivity(new Intent(this, ListaPeliculasActivity.class)));
    }
}
