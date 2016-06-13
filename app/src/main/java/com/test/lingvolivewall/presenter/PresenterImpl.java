package com.test.lingvolivewall.presenter;

import android.os.Handler;
import android.os.Message;

import com.test.lingvolivewall.model.Model;
import com.test.lingvolivewall.model.network.NetworkEvent;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.other.App;
import com.test.lingvolivewall.view.View;

import java.lang.ref.WeakReference;
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
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class PresenterImpl implements Presenter {

    public static final int PAGE_SIZE = 20;

    private static final int AUTO_REFRESH = 42;

    private static final int AUTO_REFRESH_PERIOD = 60 * 1000;

    @Inject
    Model model;

    @Inject
    @Named("IO_THREAD")
    Scheduler ioThread;

    @Inject
    @Named("UI_THREAD")
    Scheduler uiThread;

    private WeakReference<View> view;

    private CompositeSubscription compositeSubscription;

    private Handler uiHandler;

    @Override
    public void onCreate(final View view) {
        App.getComponent().inject(this);
        compositeSubscription = new CompositeSubscription();
        this.view = new WeakReference<>(view);

        compositeSubscription.add(
                model.getNetworkEventBus()
                        .subscribeOn(ioThread)
                        .observeOn(uiThread)
                        .subscribe(new Action1<NetworkEvent>() {
                            @Override
                            public void call(NetworkEvent event) {
                                handleNetworkEvent(event, view);
                            }
                        })
        );

        uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == AUTO_REFRESH) {
                    List<Post> posts = view.getPosts();
                    int size = posts == null || posts.size() < PAGE_SIZE
                            ? PAGE_SIZE
                            : posts.size();
                    fetch(size);
                    uiHandler.sendEmptyMessageDelayed(AUTO_REFRESH, AUTO_REFRESH_PERIOD);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        fetch(PAGE_SIZE);
        uiHandler.sendEmptyMessageDelayed(AUTO_REFRESH, AUTO_REFRESH_PERIOD);
    }

    @Override
    public void onStop() {
        uiHandler.removeMessages(AUTO_REFRESH);
    }

    @Override
    public void onDestroy(boolean isChangingConfigurations) {
        compositeSubscription.unsubscribe();

        if (!isChangingConfigurations) {
            model.updateDB(view.get().getPosts());
        }

        view.clear();
    }

    @Override
    public void refresh() {
        List<Post> posts = view.get().getPosts();
        int size = posts == null ? PAGE_SIZE : posts.size();
        fetch(size);
    }

    @Override
    public void loadMorePosts(int currentSize) {
        fetch(currentSize + PAGE_SIZE);
    }

    @Override
    public boolean canLoadMore() {
        return model.isConnectionOK(view.get().getContext()) && model.hasMoreElements();
    }

    private void fetch(int postNumber) {
        Subscription subscription = model.fetchPosts(postNumber)
                .observeOn(uiThread)
                .subscribeOn(ioThread)
                .subscribe(
                        new Subscriber<List<Post>>() {
                            @Override
                            public void onCompleted() {
                                view.get().stopProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                view.get().stopProgress();
                                view.get().showError(e.getMessage());
                            }

                            @Override
                            public void onNext(List<Post> posts) {
                                List<Post> old = view.get().getPosts();

                                if (old == null || old.size() == 0) {
                                    view.get().showPosts(posts);
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

                                view.get().showPosts(newPosts);
                            }
                        }
                );

        compositeSubscription.add(subscription);
    }

    private void handleNetworkEvent(NetworkEvent event, View view) {
        switch (event) {
            case SUCCESS:
                view.hideError();
                break;
            case FAILURE:
                view.showError(event.getThrowable().getMessage());
                break;
        }
    }
}
