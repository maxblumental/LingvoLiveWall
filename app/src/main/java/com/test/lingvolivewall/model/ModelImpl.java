package com.test.lingvolivewall.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.test.lingvolivewall.model.db.PostDBHelper;
import com.test.lingvolivewall.model.db.PostTable;
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

import static com.test.lingvolivewall.presenter.PresenterImpl.PAGE_SIZE;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class ModelImpl implements Model {

    /**
     * How many pages should be saved to DB for offline mode
     * when the application is closed.
     */
    private static final int PAGES_TO_SAVE = 5;

    private AtomicBoolean hasMoreElements;

    PostDBHelper dbHelper;

    LingvoLiveService lingvoLiveService;

    private Subject<NetworkEvent, NetworkEvent> networkEventBus;

    @Inject
    public ModelImpl(PostDBHelper dbHelper, LingvoLiveService lingvoLiveService) {
        this.dbHelper = dbHelper;
        this.lingvoLiveService = lingvoLiveService;
        networkEventBus = PublishSubject.create();
        hasMoreElements = new AtomicBoolean(false);
    }

    @Override
    public Observable<List<Post>> fetchPosts(int pageSize) {
        Observable<List<Post>> dbObservable = Observable.create(
                new Observable.OnSubscribe<List<Post>>() {
                    @Override
                    public void call(Subscriber<? super List<Post>> subscriber) {
                        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                        queryBuilder.setTables(PostTable.POST_TABLE);

                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        Cursor cursor = queryBuilder.query(database, null, null, null, null, null, null);

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
    public void updateDB(List<Post> posts) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        database.delete(PostTable.POST_TABLE, null, null);

        if (posts != null) {
            for (int i = 0; i < Math.min(PAGE_SIZE * PAGES_TO_SAVE, posts.size()); i++) {
                ContentValues contentValues = Post.prepareForDB(posts.get(i));
                database.insert(PostTable.POST_TABLE, null, contentValues);
            }
        }
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
