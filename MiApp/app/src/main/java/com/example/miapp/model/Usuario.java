package com.example.miapp.model;

public class Usuario {
    private String name;
    private Datos datos;

    public Usuario(String name, Datos datos) {
        this.name = name;
        this.datos = datos;
    }

    public String getName() {
        return name;
    }

    public Datos getDatos() {
        return datos;
    }

    @Override
    public boolean equals(Object o) {
        Usuario usuario = (Usuario) o;
        return datos.id == usuario.getDatos().getId();
    }

    public static class Datos {
        private String passwd;
        private int id;

        public Datos(String passwd, int id){
            this.passwd = passwd;
            this.id = id;
        }

        public String getPasswd() { return passwd; }

        public int getId() { return id; }
    }

}

