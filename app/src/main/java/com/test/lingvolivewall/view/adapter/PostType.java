package com.test.lingvolivewall.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.lingvolivewall.R;

/**
 * Created by Maxim Blumental on 6/5/2016.
 * bvmaks@gmail.com
 */
public enum PostType implements ViewHolderFactory {
    TRANSLATION_REQUEST("TranslationRequest") {
        @Override
        public PostAdapter.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            View view = inflater.inflate(R.layout.translation_request, parent, false);
            return new TranslationRequestHolder(view);
        }
    },

    USER_TRANSLATION("UserTranslation") {
        @Override
        public PostAdapter.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            View view = inflater.inflate(R.layout.user_translation, parent, false);
            return new UserTranslationHolder(view);
        }
    },

    FREE("Free") {
        @Override
        public PostAdapter.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            View view = inflater.inflate(R.layout.free, parent, false);
            return new FreeHolder(view);
        }
    },

    LOADING("Loading") {
        @Override
        public PostAdapter.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            View view = inflater.inflate(R.layout.loading, parent, false);
            return new LoadingHolder(view);
        }
    };

    private final String name;

    PostType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
