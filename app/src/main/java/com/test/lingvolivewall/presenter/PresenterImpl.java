package com.test.lingvolivewall.presenter;

import com.test.lingvolivewall.model.Model;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.other.App;
import com.test.lingvolivewall.view.View;

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
        fetchData();
    }

    private void fetchData() {
        Subscription subscription = model.fetchData()
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
                                view.showPosts(posts);
                            }
                        }
                );

        compositeSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void refresh() {
        fetchData();
    }
}
