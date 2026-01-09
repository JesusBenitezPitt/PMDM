package com.example.miapp.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.miapp.data.dao.EmpresaDAO;
import com.example.miapp.model.Empresa;

@Database(entities = {Empresa.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmpresaDAO empresaDAO();
}
