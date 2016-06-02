package com.test.lingvolivewall.model.network;

import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.model.pojo.ResponsePOJO;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public interface LingvoLiveService {

    String BASE_URL = "http://lingvolive.ru";

    @GET("/api/social/feed/page")
    Observable<ResponsePOJO> getPosts(@Query("pageSize") int pageSize);
}
