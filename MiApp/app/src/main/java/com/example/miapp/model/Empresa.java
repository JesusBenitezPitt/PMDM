package com.example.miapp.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

public class Empresa implements Serializable {
    private byte[] imagen;
    private Uri imagenUri;
    private String nombre;
    private String tipo;
    private double rating;
    private Date fecha;
    private String descripcion;
    private String pagina_web;
    private String num_telefono;
    private int userId;
    private int empresaId;

    public Empresa(byte[] idImagen, String nombre, String tipo, double rating, Date fecha, String descripcion, String pagina_web, String num_telefono, int userId) {
        this.imagen = idImagen;
        this.nombre = nombre;
        this.tipo = tipo;
        this.rating = rating;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.pagina_web = pagina_web;
        this.num_telefono = num_telefono;
        this.userId = userId;
    }

    public Empresa (byte[] idImagen, EmpresaEntity empresa){
        this.imagen = idImagen;
        this.nombre = empresa.nombre;
        this.tipo = empresa.tipo;
        this.rating = empresa.rating;
        this.fecha = empresa.fecha;
        this.descripcion = empresa.descripcion;
        this.pagina_web = empresa.paginaWeb;
        this.num_telefono = empresa.numTelefono;
        this.userId = empresa.userId;
        this.empresaId = empresa.id;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public Uri getImagenUri() {
        return imagenUri;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public double getRating() {
        return rating;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPagina_web() {
        return pagina_web;
    }

    public String getNum_telefono() {
        return num_telefono;
    }

    public int getUserId(){return userId; }

    public int getEmpresaId() { return empresaId; }
    public void setEmpresaId(int id) { this.empresaId = id; }

}
