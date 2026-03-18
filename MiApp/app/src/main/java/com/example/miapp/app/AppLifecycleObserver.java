package com.example.miapp.app;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class AppLifecycleObserver implements DefaultLifecycleObserver {

    private boolean volvioDeSegundoPlano = false;

    @Override
    public void onStop(LifecycleOwner owner) {
        volvioDeSegundoPlano = true;
    }

    @Override
    public void onStart(LifecycleOwner owner) {
    }

    public boolean consumirSegundoPlano() {
        if (volvioDeSegundoPlano) {
            volvioDeSegundoPlano = false;
            return true;
        }
        return false;
    }
}