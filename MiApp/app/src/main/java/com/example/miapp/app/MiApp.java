package com.example.miapp.app;

import android.app.Application;
import androidx.lifecycle.ProcessLifecycleOwner;

public class MiApp extends Application {

    private static AppLifecycleObserver lifecycleObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        lifecycleObserver = new AppLifecycleObserver();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);
    }

    public static boolean consumirSegundoPlano() {
        return lifecycleObserver.consumirSegundoPlano();
    }
}