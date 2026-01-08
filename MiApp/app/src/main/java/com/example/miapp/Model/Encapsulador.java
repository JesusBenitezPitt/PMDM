package com.example.miapp.Model;

import android.net.Uri;

import java.util.Date;

public class Encapsulador {
    private int imagen;
    private Uri imagenUri;
    private String empresa;
    private String texto;
    private double rating;
    private Date fecha;
    private String descripcion;
    private String pagina_web;
    private String num_telefono;
    private int userId;

    public Encapsulador(int idImagen, String empresa, String tipo, double rating, Date fecha, String descripcion, String pagina_web, String num_telefono, int userId) {
        this.imagen = idImagen;
        this.empresa = empresa;
        this.texto = tipo;
        this.rating = rating;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.pagina_web = pagina_web;
        this.num_telefono = num_telefono;
        this.userId = userId;
    }

    public Encapsulador(Uri imagenUri, String empresa, String tipo, double rating, Date fecha) {
        this.imagenUri = imagenUri;
        this.empresa = empresa;
        this.texto = tipo;
        this.rating = rating;
        this.fecha = fecha;
        this.imagen = 0;
    }

    public int getImagenId() {
        return imagen;
    }

    public Uri getImagenUri() {
        return imagenUri;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getTipo() {
        return texto;
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

}
