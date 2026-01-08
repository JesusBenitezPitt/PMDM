package com.example.miapp.Controller;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.miapp.Model.Empresa;
import com.example.miapp.Model.EmpresaDAO;

@Database(entities = {Empresa.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmpresaDAO empresaDAO();
}

// TODO: No funciona, revisar
