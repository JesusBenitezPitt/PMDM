package com.example.miapp.Model;

public class Usuario {
    private String name;
    private String passwd;

    public Usuario(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public String getPasswd() {
        return passwd;
    }

    @Override
    public boolean equals(Object o) {
        Usuario usuario = (Usuario) o;
        return name.equals(usuario.name) && passwd.equals(usuario.passwd);
    }

}

