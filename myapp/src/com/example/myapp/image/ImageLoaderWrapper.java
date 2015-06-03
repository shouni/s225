package com.example.myapp.image;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 *
 */
public class ImageLoaderWrapper {

    public static void load(Context context, String url, ImageView view) {
        view.setVisibility(View.INVISIBLE);
        view.setTag(url);
        UpdateImageViewTask imgTask = new UpdateImageViewTask(context, url, view);
        imgTask.execute();
    }

}
