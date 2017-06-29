package com.example.lenovo.comoney;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by lenovo on 2016-11-09.
 */

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
