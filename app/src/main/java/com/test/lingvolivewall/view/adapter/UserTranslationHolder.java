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
class UserTranslationHolder extends PostAdapter.ViewHolder {

    @Bind(R.id.heading)
    TextView heading;

    @Bind(R.id.translation)
    TextView translation;

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.id)
    TextView id;

    public UserTranslationHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    void bindPost(Post post) {
        String name = post.getAuthor().getName();
        String translation = post.getTranslation();
        String heading = post.getHeading();
        int postDbId = post.getPostDbId();

        author.setText(name);
        this.translation.setText(translation);
        this.heading.setText(heading);
        this.id.setText(Integer.toString(postDbId));
    }
}
