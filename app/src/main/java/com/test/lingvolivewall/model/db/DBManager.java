package com.test.lingvolivewall.model.db;

import com.test.lingvolivewall.model.pojo.Post;

import java.util.List;

/**
 * Helps to access to the database.
 */
public interface DBManager {
    List<Post> getAllPosts();

    void deleteAll();

    void insertPosts(List<Post> posts);
}
