package com.test.lingvolivewall.other.di;

import com.test.lingvolivewall.model.network.LingvoLiveService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Module
public class ModelModule {
    @Provides
    LingvoLiveService getRemoteService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LingvoLiveService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(LingvoLiveService.class);
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
