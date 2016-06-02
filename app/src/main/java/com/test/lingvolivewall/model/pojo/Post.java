package com.test.lingvolivewall.model.pojo;

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
}
