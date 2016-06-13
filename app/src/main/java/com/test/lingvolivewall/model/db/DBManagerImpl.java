package com.test.lingvolivewall.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.test.lingvolivewall.model.pojo.Post;

import java.util.ArrayList;
import java.util.List;

public class DBManagerImpl implements DBManager {

    private PostDBHelper dbHelper;

    public DBManagerImpl(PostDBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<Post> getAllPosts() {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(PostTable.POST_TABLE);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(database, null, null, null, null, null, null);

        List<Post> posts = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                posts.add(Post.create(cursor));
            }
        } finally {
            cursor.close();
        }
        return posts;
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(PostTable.POST_TABLE, null, null);
    }

    @Override
    public void insertPosts(List<Post> posts) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        if (posts != null) {
            for (int i = 0; i < posts.size(); i++) {
                ContentValues contentValues = Post.prepareForDB(posts.get(i));
                database.insert(PostTable.POST_TABLE, null, contentValues);
            }
        }
    }
}
