package com.example.miapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Empresas")
public class EmpresaEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "tipo")
    public String tipo;

    @ColumnInfo(name = "rating")
    public double rating;

    @ColumnInfo(name = "fecha")
    public Date fecha;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "pagina_web")
    public String paginaWeb;

    @ColumnInfo(name = "num_telefono")
    public String numTelefono;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "imagen", typeAffinity = ColumnInfo.BLOB)
    public byte[] imagen;

    public EmpresaEntity(byte[] imagen, String nombre, String tipo, double rating, Date fecha, String descripcion, String paginaWeb, String numTelefono, int userId) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.tipo = tipo;
        this.rating = rating;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.paginaWeb = paginaWeb;
        this.numTelefono = numTelefono;
        this.userId = userId;
    }

    public EmpresaEntity() {}
}

