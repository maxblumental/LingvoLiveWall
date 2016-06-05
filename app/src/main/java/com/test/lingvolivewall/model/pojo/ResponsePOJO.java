package com.test.lingvolivewall.model.pojo;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class ResponsePOJO {

    private Post[] posts;

    private boolean hasMorePosts;

    public Post[] getPosts() {
        return posts;
    }

    public void setPosts(Post[] posts) {
        this.posts = posts;
    }

    public boolean hasMorePosts() {
        return hasMorePosts;
    }

    public void setHasMorePosts(boolean hasMorePosts) {
        this.hasMorePosts = hasMorePosts;
    }
}
