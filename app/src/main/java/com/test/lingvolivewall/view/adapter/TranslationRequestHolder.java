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
class TranslationRequestHolder extends PostAdapter.ViewHolder {

    @Bind(R.id.heading)
    TextView heading;

    @Bind(R.id.author)
    TextView author;

    public TranslationRequestHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    void bindPost(Post post) {
        String name = post.getAuthor().getName();
        String heading = post.getHeading();

        author.setText(name);
        this.heading.setText(heading);
    }
}
