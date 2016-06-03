package com.test.lingvolivewall.model;

import android.content.Context;
import android.database.Cursor;

import com.test.lingvolivewall.model.db.PostProvider;
import com.test.lingvolivewall.model.db.PostTable;
import com.test.lingvolivewall.model.network.LingvoLiveService;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.model.pojo.ResponsePOJO;
import com.test.lingvolivewall.other.App;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class ModelImpl implements Model {

    @Inject
    LingvoLiveService lingvoLiveService;

    @Inject
    @Named("IO_THREAD")
    Scheduler ioThread;

    @Inject
    @Named("UI_THREAD")
    Scheduler uiThread;

    public ModelImpl() {
        App.getComponent().inject(this);
    }

    @Override
    public Observable<List<Post>> fetchPosts(final Context context, int pageSize) {
        Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                Cursor cursor = context.getContentResolver().query(PostProvider.CONTENT_URI, null, null, null, null);

                if (cursor == null) {
                    subscriber.onCompleted();
                }

                try {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex(PostTable.COLUMN_POST_ID));
                        String heading = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_HEADING));
                        String author = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_AUTHOR));
//                        String author = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_AUTHOR));
//                        String author = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_AUTHOR));
//                        String author = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_AUTHOR));
                    }
                } finally {
                    cursor.close();
                }
            }
        });
        return lingvoLiveService.getPosts(pageSize)
                .map(new Func1<ResponsePOJO, List<Post>>() {
                    @Override
                    public List<Post> call(ResponsePOJO responsePOJO) {
                        return Arrays.asList(responsePOJO.getPosts());
                    }
                })
                .subscribeOn(ioThread)
                .observeOn(uiThread);
    }
}
