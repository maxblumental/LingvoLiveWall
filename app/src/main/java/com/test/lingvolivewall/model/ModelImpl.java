package com.test.lingvolivewall.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.test.lingvolivewall.model.db.DBManager;
import com.test.lingvolivewall.model.network.LingvoLiveService;
import com.test.lingvolivewall.model.network.NetworkEvent;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.model.pojo.ResponsePOJO;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class ModelImpl implements Model {

    private AtomicBoolean hasMoreElements;

    LingvoLiveService lingvoLiveService;

    private DBManager dbManager;

    private Subject<NetworkEvent, NetworkEvent> networkEventBus;

    @Inject
    public ModelImpl(LingvoLiveService lingvoLiveService, DBManager dbManager) {
        this.lingvoLiveService = lingvoLiveService;
        this.dbManager = dbManager;
        networkEventBus = PublishSubject.create();
        hasMoreElements = new AtomicBoolean(false);
    }

    @Override
    public Observable<List<Post>> fetchPosts(int pageSize) {
        Observable<List<Post>> dbObservable = Observable.create(
                new Observable.OnSubscribe<List<Post>>() {
                    @Override
                    public void call(Subscriber<? super List<Post>> subscriber) {
                        List<Post> posts = dbManager.getAllPosts();
                        subscriber.onNext(posts);
                        subscriber.onCompleted();
                    }
                });

        Observable<List<Post>> networkObservable = lingvoLiveService.getPosts(pageSize)
                .map(new Func1<ResponsePOJO, List<Post>>() {
                    @Override
                    public List<Post> call(ResponsePOJO responsePOJO) {
                        ModelImpl.this.hasMoreElements.set(responsePOJO.hasMorePosts());
                        return Arrays.asList(responsePOJO.getPosts());
                    }
                });

        return networkObservable
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        networkEventBus.onNext(NetworkEvent.SUCCESS);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        NetworkEvent event = NetworkEvent.FAILURE;
                        event.setThrowable(throwable);
                        networkEventBus.onNext(event);
                    }
                })
                .onErrorResumeNext(dbObservable);
    }

    @Override
    public void updateDB(List<Post> posts) {
        dbManager.deleteAll();
        dbManager.insertPosts(posts);
    }

    @Override
    public Observable<NetworkEvent> getNetworkEventBus() {
        return networkEventBus;
    }

    @Override
    public boolean isConnectionOK(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean hasMoreElements() {
        return hasMoreElements.get();
    }
}
