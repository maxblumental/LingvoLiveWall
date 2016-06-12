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

import rx.Observable;
import rx.Subscriber;

public class ModelImplTest {

    private final int PAGE_SIZE = 2;

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
                        responsePOJO.setPosts(new Post[]{
                                new Post(), new Post()
                        });
                        subscriber.onNext(responsePOJO);
                    }
                }));

        //model.fetchPosts()
    }

    @Test
    public void hasMoreElements() throws Exception {

    }

}