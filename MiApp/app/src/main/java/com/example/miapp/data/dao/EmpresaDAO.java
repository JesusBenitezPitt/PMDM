package com.example.miapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.miapp.model.EmpresaEntity;

import java.util.List;

@Dao
public interface EmpresaDAO {

    @Insert
    long insertar(EmpresaEntity empresa);

    @Insert
    List<Long> insertarTodas(List<EmpresaEntity> empresas);

    @Update
    int actualizar(EmpresaEntity empresa);

    @Delete
    int eliminar(EmpresaEntity empresa);

    @Query("SELECT * FROM empresas WHERE user_id = :userId")
    List<EmpresaEntity> obtenerEmpresas(int userId);

    @Query("SELECT * FROM empresas")
    List<EmpresaEntity> obtenerTodas();

}
