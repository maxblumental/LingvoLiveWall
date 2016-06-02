package com.test.lingvolivewall.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.lingvolivewall.R;
import com.test.lingvolivewall.model.pojo.Post;

import java.util.List;

/**
 * Provides elements for the list of posts.
 * <p/>
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        String type = posts.get(position).getPostType();

        if (type.equals(PostType.TRANSLATION_REQUEST.name)) {
            return PostType.TRANSLATION_REQUEST.ordinal();
        } else if (type.equals(PostType.USER_TRANSLATION.name)) {
            return PostType.USER_TRANSLATION.ordinal();
        } else if (type.equals(PostType.FREE.name)) {
            return PostType.FREE.ordinal();
        } else {
            throw new RuntimeException("getItemViewType(): Unknown type of post!");
        }
    }

    enum PostType {
        TRANSLATION_REQUEST("TranslationRequest"),
        USER_TRANSLATION("UserTranslation"),
        FREE("Free");

        private String name;

        PostType(String name) {
            this.name = name;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        PostType postType = PostType.values()[viewType];

        switch (postType) {
            case TRANSLATION_REQUEST:
                View translationRequestPostView = inflater.inflate(R.layout.translation_request, parent, false);
                return new TranslationRequestHolder(translationRequestPostView);
            case USER_TRANSLATION:
                View userTranslationPostView = inflater.inflate(R.layout.user_translation, parent, false);
                return new UserTranslationHolder(userTranslationPostView);
            case FREE:
                View freePostView = inflater.inflate(R.layout.free, parent, false);
                return new FreeHolder(freePostView);
        }

        throw new RuntimeException("onCreateViewHolder(): Unknown type of post!");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bindPost(post);
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    abstract static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bindPost(Post post);
    }
}
