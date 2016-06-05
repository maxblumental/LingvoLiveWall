package com.test.lingvolivewall.other.di;

import com.test.lingvolivewall.model.Model;
import com.test.lingvolivewall.model.ModelImpl;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Module
public class PresenterModule {
    @Provides
    @Singleton
    Model getModel() {
        return new ModelImpl();
    }

    @Provides
    @Named("UI_THREAD")
    Scheduler getUiScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named("IO_THREAD")
    Scheduler getIoScheduler() {
        return Schedulers.io();
    }
}
