package com.test.lingvolivewall.other.di;

import com.test.lingvolivewall.model.ModelImpl;
import com.test.lingvolivewall.presenter.PresenterImpl;
import com.test.lingvolivewall.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Component(modules = {ViewModule.class, PresenterModule.class, ModelModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(MainActivity activity);

    void inject(PresenterImpl presenter);

    void inject(ModelImpl model);
}
