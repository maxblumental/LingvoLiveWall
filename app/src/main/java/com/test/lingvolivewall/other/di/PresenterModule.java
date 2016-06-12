package com.test.lingvolivewall.other.di;

import com.test.lingvolivewall.model.Model;
import com.test.lingvolivewall.model.ModelImpl;
import com.test.lingvolivewall.model.db.PostDBHelper;
import com.test.lingvolivewall.model.network.LingvoLiveService;
import com.test.lingvolivewall.model.pojo.Post;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Module
public class PresenterModule {
    @Provides
    @Singleton
    Model getModel(PostDBHelper postDBHelper, LingvoLiveService lingvoLiveService) {
        return new ModelImpl(postDBHelper, lingvoLiveService);
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
