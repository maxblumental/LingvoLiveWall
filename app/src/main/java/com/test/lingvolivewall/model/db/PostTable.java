package com.test.lingvolivewall.model.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Maxim Blumental on 6/3/2016.
 * bvmaks@gmail.com
 */
public class PostTable {
    public static final String POST_TABLE = "post_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_POST_ID = "postDbId";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_TRANSLATION = "translation";
    public static final String COLUMN_HEADING = "heading";
    public static final String COLUMN_POST_TYPE = "post_type";

    private static final String DATABASE_CREATE = "create table "
            + POST_TABLE + "( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_POST_ID + " text, "
            + COLUMN_AUTHOR + " text not null, "
            + COLUMN_MESSAGE + " text, "
            + COLUMN_TRANSLATION + " text,"
            + COLUMN_HEADING + " text,"
            + COLUMN_POST_TYPE + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(PostTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + POST_TABLE);
        onCreate(database);
    }
}
