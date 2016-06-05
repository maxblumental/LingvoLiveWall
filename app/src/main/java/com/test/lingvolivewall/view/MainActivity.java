package com.test.lingvolivewall.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.test.lingvolivewall.R;
import com.test.lingvolivewall.model.pojo.Post;
import com.test.lingvolivewall.other.App;
import com.test.lingvolivewall.presenter.Presenter;
import com.test.lingvolivewall.view.adapter.PostAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements View, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.connectionIndicator)
    TextView connectionIndicator;

    @Bind(R.id.postList)
    RecyclerView postList;

    private PostAdapter postAdapter;

    @Inject
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getComponent().inject(this);
        refreshLayout.setOnRefreshListener(this);

        postAdapter = new PostAdapter(this, presenter);
        postList.setLayoutManager(new LinearLayoutManager(this));
        postList.setAdapter(postAdapter);

        presenter.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy(isChangingConfigurations());
        super.onDestroy();
    }

    @Override
    public void showPosts(List<Post> posts) {
        postAdapter.setPosts(posts);
    }

    @Override
    public void showError(String message) {
        connectionIndicator.setVisibility(android.view.View.VISIBLE);
        connectionIndicator.setText(message);
    }

    @Override
    public void hideError() {
        connectionIndicator.setVisibility(android.view.View.GONE);
    }

    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public List<Post> getPosts() {
        return postAdapter.getPosts();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }


}
