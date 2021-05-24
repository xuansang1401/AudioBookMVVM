package com.t3h.basemvvm.util;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.Book;

import java.util.ArrayList;
import java.util.List;

public class AppUtil {
    public static void imageFromUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_launcher_background)
                .into(imageView);
    }

    public static void setVisibilityView(View view, boolean isVis){
        if (isVis)view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);

    }


}
