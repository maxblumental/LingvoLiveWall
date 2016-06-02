package com.test.lingvolivewall.other.di;

import com.test.lingvolivewall.presenter.Presenter;
import com.test.lingvolivewall.presenter.PresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Module
public class ViewModule {
    @Provides
    Presenter getPresenter() {
        return new PresenterImpl();
    }
}
