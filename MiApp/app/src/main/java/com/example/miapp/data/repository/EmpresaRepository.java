package com.example.miapp.data.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.miapp.data.dao.EmpresaDAO;
import com.example.miapp.data.database.AppDatabase;
import com.example.miapp.model.EmpresaEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmpresaRepository {

    private final EmpresaDAO empresaDAO;
    private final ExecutorService executor;

    public EmpresaRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "secure-ops")
                .build();
        empresaDAO = db.empresaDAO();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insertarEmpresa(EmpresaEntity empresa, Callback<Long> callback) {
        executor.execute(() -> {
            long id = empresaDAO.insertar(empresa);
            if (callback != null) callback.onComplete(id);
        });
    }

    public void insertarEmpresas(List<EmpresaEntity> empresas, Callback<List<Long>> callback) {
        executor.execute(() -> {
            List<Long> ids = empresaDAO.insertarTodas(empresas);
            if (callback != null) callback.onComplete(ids);
        });
    }

    public void actualizarEmpresas(EmpresaEntity empresa, Callback<Integer> callback) {
        executor.execute(() -> {
            int rows = empresaDAO.actualizar(empresa);
            if (callback != null) callback.onComplete(rows);
        });
    }

    public void eliminarEmpresas(EmpresaEntity empresa, Callback<Integer> callback) {
        executor.execute(() -> {
            int rows = empresaDAO.eliminar(empresa);
            if (callback != null) callback.onComplete(rows);
        });
    }

    public void obtenerEmpresasID(int userId, Callback<List<EmpresaEntity>> callback) {
        executor.execute(() -> {
            List<EmpresaEntity> empresas = empresaDAO.obtenerEmpresas(userId);
            if (callback != null) callback.onComplete(empresas);
        });
    }

    public void obtenerTodasLasEmpresas(Callback<List<EmpresaEntity>> callback) {
        executor.execute(() -> {
            List<EmpresaEntity> empresas = empresaDAO.obtenerTodas();
            if (callback != null) callback.onComplete(empresas);
        });
    }

    public interface Callback<T> {
        void onComplete(T result);
    }
}
