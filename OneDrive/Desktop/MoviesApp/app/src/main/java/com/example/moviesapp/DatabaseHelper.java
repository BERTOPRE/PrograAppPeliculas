package com.example.moviesapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "peliculas.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE articulos (" +
                "codigo INTEGER PRIMARY KEY, " +
                "nombre_articulo TEXT, " +
                "existencias INTEGER, " +
                "precio_costo REAL, " +
                "precio_venta REAL, " +
                "categoria TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS articulos");
        onCreate(db);
    }

    // Insertar una película
    public boolean insertarArticulo(int codigo, String nombre, int existencias, double costo, double venta, String categoria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", codigo);
        values.put("nombre_articulo", nombre);
        values.put("existencias", existencias);
        values.put("precio_costo", costo);
        values.put("precio_venta", venta);
        values.put("categoria", categoria);
        long result = db.insert("articulos", null, values);
        return result != -1;
    }

    // Buscar por código
    public Cursor buscarArticulo(int codigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM articulos WHERE codigo = ?", new String[]{String.valueOf(codigo)});
    }

    // Eliminar por código
    public boolean eliminarArticulo(int codigo) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete("articulos", "codigo = ?", new String[]{String.valueOf(codigo)});
        return filas > 0;
    }
}
