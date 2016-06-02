package com.test.lingvolivewall.other.di;

import com.test.lingvolivewall.view.MainActivity;

import dagger.Component;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Component(modules = {ViewModule.class})
public interface ApplicationComponent {
    void inject(MainActivity activity);
}
