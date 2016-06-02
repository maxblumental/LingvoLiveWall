package com.test.lingvolivewall.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.test.lingvolivewall.R;
import com.test.lingvolivewall.model.pojo.Post;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */

class FreeHolder extends PostAdapter.ViewHolder {

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.message)
    TextView message;

    @Bind(R.id.id)
    TextView id;

    public FreeHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    void bindPost(Post post) {
        String author = post.getAuthor().getName();
        String message = post.getMessage();
        int postDbId = post.getPostDbId();

        this.author.setText(author);
        this.message.setText(message);
        this.id.setText(Integer.toString(postDbId));
    }
}
