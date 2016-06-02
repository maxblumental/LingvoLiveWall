package com.test.lingvolivewall.other.di;

import com.test.lingvolivewall.presenter.PresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Module
public class ViewModule {
    @Provides
    PresenterImpl getPresenter() {
        return new PresenterImpl();
    }
}
