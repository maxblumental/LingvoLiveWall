package com.test.lingvolivewall.other.di;

import com.test.lingvolivewall.model.Model;
import com.test.lingvolivewall.model.ModelImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Module
public class PresenterModule {
    @Provides
    CompositeSubscription getCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    Model getModel() {
        return new ModelImpl();
    }
}
