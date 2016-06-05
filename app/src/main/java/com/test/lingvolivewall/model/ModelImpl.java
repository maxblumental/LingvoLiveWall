package com.test.lingvolivewall.model;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.test.lingvolivewall.model.db.PostProvider;
import com.test.lingvolivewall.model.network.LingvoLiveService;
import com.test.lingvolivewall.model.network.NetworkEvent;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.model.pojo.ResponsePOJO;
import com.test.lingvolivewall.other.App;

import java.util.ArrayList;
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

    @Inject
    LingvoLiveService lingvoLiveService;

    private Subject<NetworkEvent, NetworkEvent> networkEventBus;

    public ModelImpl() {
        App.getComponent().inject(this);
        networkEventBus = PublishSubject.create();
        hasMoreElements = new AtomicBoolean(false);
    }

    @Override
    public Observable<List<Post>> fetchPosts(final Context context, int pageSize) {
        Observable<List<Post>> dbObservable = Observable.create(
                new Observable.OnSubscribe<List<Post>>() {
                    @Override
                    public void call(Subscriber<? super List<Post>> subscriber) {
                        Cursor cursor = context.getContentResolver().query(PostProvider.CONTENT_URI,
                                null, null, null, null);

                        if (cursor == null) {
                            subscriber.onCompleted();
                        }

                        List<Post> posts = new ArrayList<>();

                        try {
                            while (cursor.moveToNext()) {
                                posts.add(Post.create(cursor));
                            }
                        } finally {
                            cursor.close();
                        }

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
