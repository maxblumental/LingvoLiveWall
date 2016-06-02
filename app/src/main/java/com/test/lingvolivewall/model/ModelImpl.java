package com.test.lingvolivewall.model;

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
    public Observable<List<Post>> fetchData() {
        return lingvoLiveService.getPosts(10)
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
