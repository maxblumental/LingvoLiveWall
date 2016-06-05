package com.test.lingvolivewall.view;

import android.content.Context;

import com.test.lingvolivewall.model.pojo.Post;

import java.util.List;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public interface View {
    void showPosts(List<Post> posts);

    void showError(String message);

    void hideError();

    void showProgress();

    void stopProgress();

    List<Post> getPosts();

    Context getContext();
}
