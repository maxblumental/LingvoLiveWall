package com.test.lingvolivewall.presenter;

import android.content.ContentValues;
import android.content.Context;

import com.test.lingvolivewall.model.Model;
import com.test.lingvolivewall.model.db.PostProvider;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.other.App;
import com.test.lingvolivewall.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class PresenterImpl implements Presenter {

    private static final int PAGE_SIZE = 20;

    private static final int PAGES_TO_SAVE = 5;

    @Inject
    Model model;

    @Inject
    @Named("IO_THREAD")
    Scheduler ioThread;

    @Inject
    @Named("UI_THREAD")
    Scheduler uiThread;

    private View view;

    @Inject
    CompositeSubscription compositeSubscription;

    @Override
    public void onCreate(View view) {
        App.getComponent().inject(this);
        this.view = view;
    }

    @Override
    public void onResume(Context context) {
        fetch(context, PAGE_SIZE);
    }

    @Override
    public void onDestroy(Context context, boolean isChangingConfigurations) {
        compositeSubscription.unsubscribe();

        if (!isChangingConfigurations) {
            context.getContentResolver().delete(PostProvider.CONTENT_URI, null, null);

            List<Post> posts = view.getPosts();

            if (posts != null) {
                for (int i = 0; i < Math.min(PAGE_SIZE * PAGES_TO_SAVE, posts.size()); i++) {
                    ContentValues contentValues = Post.prepareForDB(posts.get(i));
                    context.getContentResolver().insert(PostProvider.CONTENT_URI, contentValues);
                }
            }
        }

        view = null;
    }

    @Override
    public void refresh(Context context) {
        List<Post> posts = view.getPosts();
        int size = posts == null ? PAGE_SIZE : posts.size();
        fetch(context, size);
    }

    @Override
    public void onBottomReached(Context context, int currentSize) {
        fetch(context, currentSize + PAGE_SIZE);
    }

    private void fetch(Context context, int postNumber) {
        Subscription subscription = model.fetchPosts(context, postNumber)
                .observeOn(uiThread)
                .subscribeOn(ioThread)
                .subscribe(
                        new Subscriber<List<Post>>() {
                            @Override
                            public void onCompleted() {
                                view.stopProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                view.stopProgress();
                                view.showError(e.toString());
                            }

                            @Override
                            public void onNext(List<Post> posts) {
                                List<Post> old = view.getPosts();

                                if (old == null || old.size() == 0) {
                                    view.showPosts(posts);
                                    return;
                                }

                                HashSet<Post> oldSet = new HashSet<>(old);

                                oldSet.addAll(posts);

                                ArrayList<Post> newPosts = new ArrayList<>(oldSet);
                                Collections.sort(newPosts, new Comparator<Post>() {
                                    @Override
                                    public int compare(Post lhs, Post rhs) {
                                        return rhs.getPostDbId() - lhs.getPostDbId();
                                    }
                                });

                                view.showPosts(newPosts);
                            }
                        }
                );

        compositeSubscription.add(subscription);
    }
}
