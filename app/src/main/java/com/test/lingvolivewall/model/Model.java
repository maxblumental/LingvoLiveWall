package com.test.lingvolivewall.model;

import android.content.Context;

import com.test.lingvolivewall.model.pojo.Post;

import java.util.List;

import rx.Observable;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public interface Model {
    Observable<List<Post>> fetchPosts(Context context, int pageSize);
}
