package com.test.lingvolivewall.presenter;

import com.test.lingvolivewall.model.Model;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.other.App;
import com.test.lingvolivewall.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class PresenterImpl implements Presenter {

    private static final int PAGE_SIZE = 20;

    @Inject
    Model model;

    private View view;

    @Inject
    CompositeSubscription compositeSubscription;

    @Override
    public void onCreate(View view) {
        App.getComponent().inject(this);
        this.view = view;
    }

    @Override
    public void onResume() {
        fetch(PAGE_SIZE);
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void refresh() {
        fetch(view.getPosts().size());
    }

    @Override
    public void onBottomReached(int currentSize) {
        fetch(currentSize + PAGE_SIZE);
    }

    private void fetch(int postNumber) {
        Subscription subscription = model.fetchData(postNumber)
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
