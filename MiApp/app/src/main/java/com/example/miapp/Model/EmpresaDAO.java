package com.example.miapp.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmpresaDAO {

    @Insert
    void insert(Empresa empresa);

    @Insert
    void insertAll(List<Empresa> empresas);

    @Query("SELECT * FROM empresas WHERE user_id IN (:userId)")
    List<Empresa> getAll(int userId);
}
