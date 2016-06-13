package com.test.lingvolivewall.other.di;

import android.content.Context;

import com.test.lingvolivewall.model.db.DBManager;
import com.test.lingvolivewall.model.db.DBManagerImpl;
import com.test.lingvolivewall.model.db.PostDBHelper;
import com.test.lingvolivewall.model.network.LingvoLiveService;

import java.lang.ref.WeakReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
@Module
public class ModelModule {

    public ModelModule(WeakReference<Context> contextWeakReference) {
        this.contextWeakReference = contextWeakReference;
    }

    private WeakReference<Context> contextWeakReference;

    @Provides
    @Singleton
    LingvoLiveService getRemoteService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LingvoLiveService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(LingvoLiveService.class);
    }

    @Provides
    @Singleton
    DBManager getDBManager() {
        return new DBManagerImpl(new PostDBHelper(contextWeakReference.get()));
    }
}
