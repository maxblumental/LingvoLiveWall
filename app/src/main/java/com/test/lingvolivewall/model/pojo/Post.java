package com.test.lingvolivewall.model.pojo;

import android.content.ContentValues;
import android.database.Cursor;

import com.test.lingvolivewall.model.db.PostTable;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class Post {

    private int postDbId;

    public int getPostDbId() {
        return postDbId;
    }

    public void setPostDbId(int postDbId) {
        this.postDbId = postDbId;
    }

    private String message;

    private String translation;

    private Author author;

    private String heading;

    private String postType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    @Override
    public String toString() {
        return "ClassPojo [translation = " + translation + ", author = " + author + ", postType = "
                + postType + ", heading = " + heading + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Post) {
            return ((Post) o).getPostDbId() == postDbId;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return postDbId;
    }

    /**
     * Factory method used for extracting posts
     * from the database.
     *
     * @param cursor - a cursor pointing to a row with Post's fields
     */
    public static Post create(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(PostTable.COLUMN_POST_ID));
        String heading = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_HEADING));
        String authorName = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_AUTHOR));
        String message = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_MESSAGE));
        String translation = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_TRANSLATION));
        String postType = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_POST_TYPE));

        Post post = new Post();
        Author author = new Author();

        author.setName(authorName);
        post.setAuthor(author);
        post.setHeading(heading);
        post.setMessage(message);
        post.setTranslation(translation);
        post.setPostDbId(id);
        post.setPostType(postType);

        return post;
    }

    public static ContentValues prepareForDB(Post post) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PostTable.COLUMN_POST_TYPE, post.getPostType());
        contentValues.put(PostTable.COLUMN_AUTHOR, post.getAuthor().getName());
        contentValues.put(PostTable.COLUMN_HEADING, post.getHeading());
        contentValues.put(PostTable.COLUMN_POST_ID, post.getPostDbId());
        contentValues.put(PostTable.COLUMN_MESSAGE, post.getMessage());
        contentValues.put(PostTable.COLUMN_TRANSLATION, post.getTranslation());

        return contentValues;
    }
}
