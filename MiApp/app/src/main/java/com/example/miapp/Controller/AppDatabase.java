package com.example.miapp.Controller;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.miapp.Model.Empresa;
import com.example.miapp.Model.EmpresaDAO;

@Database(entities = {Empresa.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmpresaDAO empresaDAO();
}


// TODO: Revisar como podemos guardar una imagen en ROOM.
