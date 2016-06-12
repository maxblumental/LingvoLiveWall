package com.test.lingvolivewall.model;

import com.test.lingvolivewall.model.network.LingvoLiveService;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.model.pojo.ResponsePOJO;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

public class ModelImplTest {

    private final int PAGE_SIZE = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    LingvoLiveService lingvoLiveService;

    @InjectMocks
    ModelImpl model;

    @Test
    public void fetchPosts() throws Exception {
        Mockito.when(lingvoLiveService.getPosts(PAGE_SIZE))
                .thenReturn(Observable.create(new Observable.OnSubscribe<ResponsePOJO>() {
                    @Override
                    public void call(Subscriber<? super ResponsePOJO> subscriber) {
                        final ResponsePOJO responsePOJO = new ResponsePOJO();
                        responsePOJO.setHasMorePosts(true);

                        Post post = new Post();
                        post.setPostDbId(1);

                        responsePOJO.setPosts(new Post[]{post});

                        subscriber.onNext(responsePOJO);
                        subscriber.onCompleted();
                    }
                }));

        Observable<List<Post>> observable = model.fetchPosts(PAGE_SIZE);
        TestSubscriber<List<Post>> testSubscriber = new TestSubscriber<>();

        observable.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();

        ArrayList<List<Post>> items = new ArrayList<>();
        Post post = new Post();
        post.setPostDbId(1);
        items.add(Collections.singletonList(post));

        testSubscriber.assertReceivedOnNext(items);
    }
}