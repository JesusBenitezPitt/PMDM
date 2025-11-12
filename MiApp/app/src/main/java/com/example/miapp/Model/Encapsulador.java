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
    private boolean acabado;

    public Encapsulador(int idImagen, String empresa, String tipo, double rating, Date fecha) {
        this.imagen = idImagen;
        this.empresa = empresa;
        this.texto = tipo;
        this.rating = rating;
        this.fecha = fecha;
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
}
