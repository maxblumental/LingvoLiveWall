package com.test.lingvolivewall.presenter;

import com.test.lingvolivewall.view.View;

/**
 * Created by Maxim Blumental on 6/2/2016.
 * bvmaks@gmail.com
 */
public interface Presenter {
    void onCreate(View view);

    void onResume();

    void onDestroy();

    void refresh();
}
