package com.test.lingvolivewall.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maxim Blumental on 6/3/2016.
 * bvmaks@gmail.com
 */
public class PostDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "posts.db";
    private static final int DATABASE_VERSION = 1;

    public PostDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PostTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PostTable.onUpgrade(db, oldVersion, newVersion);
    }
}
