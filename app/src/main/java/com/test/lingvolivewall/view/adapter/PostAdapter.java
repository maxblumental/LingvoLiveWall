package com.test.lingvolivewall.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.presenter.Presenter;

import java.util.HashMap;
import java.util.List;

/**
 * Provides elements for the list of posts.
 * <p/>
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Presenter presenter;

    private List<Post> posts;

    private HashMap<String, PostType> postTypeMap;

    public PostAdapter(Presenter presenter) {
        this.presenter = presenter;

        postTypeMap = new HashMap<>();
        for (PostType type : PostType.values()) {
            postTypeMap.put(type.getName(), type);
        }
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == posts.size() && presenter.canLoadMore()) {
            return PostType.LOADING.ordinal();
        }

        String typeName = posts.get(position).getPostType();
        PostType postType = postTypeMap.get(typeName);

        if (postType != null) {
            return postType.ordinal();
        } else {
            throw new IllegalStateException("getItemViewType(): Unknown type of post!");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        PostType postType = PostType.values()[viewType];

        return postType.createViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == PostType.LOADING.ordinal()) {
            presenter.loadMorePosts(posts.size());
        }

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
