package com.example.miapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.miapp.model.Empresa;

import java.util.List;

@Dao
public interface EmpresaDAO {

    @Insert
    long insertar(Empresa empresa);

    @Insert
    List<Long> insertarTodas(List<Empresa> empresas);

    @Update
    int actualizar(Empresa empresa);

    @Delete
    int eliminar(Empresa empresa);

    @Query("SELECT * FROM empresas WHERE user_id = :userId")
    List<Empresa> obtenerEmpresas(int userId);

    @Query("SELECT * FROM empresas")
    List<Empresa> obtenerTodas();

}
