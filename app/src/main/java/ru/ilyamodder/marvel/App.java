package ru.ilyamodder.marvel;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ilya on 08.11.16.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
