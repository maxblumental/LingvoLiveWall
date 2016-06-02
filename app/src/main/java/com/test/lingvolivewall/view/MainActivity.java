package com.test.lingvolivewall.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.test.lingvolivewall.R;
import com.test.lingvolivewall.presenter.Presenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View {

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
    }
}
