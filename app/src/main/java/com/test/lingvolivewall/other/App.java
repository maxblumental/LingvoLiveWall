package com.test.lingvolivewall.other;

import android.app.Application;

import com.test.lingvolivewall.other.di.ApplicationComponent;
import com.test.lingvolivewall.other.di.DaggerApplicationComponent;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class App extends Application {

    private static ApplicationComponent applicationComponent;

    public static ApplicationComponent getComponent() {
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        build();
    }

    private void build() {
        applicationComponent = DaggerApplicationComponent.builder().build();
    }
}
