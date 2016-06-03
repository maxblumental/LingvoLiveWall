package com.test.lingvolivewall.presenter;

import android.content.Context;

import com.test.lingvolivewall.view.View;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public interface Presenter {
    void onCreate(View view);

    void onResume(Context context);

    void onDestroy(Context context, boolean isChangingConfigurations);

    void refresh(Context context);

    void onBottomReached(Context context, int currentSize);
}
