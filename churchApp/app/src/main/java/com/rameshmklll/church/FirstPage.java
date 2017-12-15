package com.rameshmklll.church;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


public class FirstPage extends Fragment {
    View mv;
    Activity activity;
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.mt, R.drawable.mt, R.drawable.mt, R.drawable.mt, R.drawable.mt};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mv = inflater.inflate(R.layout.fragment_first_page, null);
        carouselView = (CarouselView)mv. findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };
        carouselView.setImageListener(imageListener);
        activity = getActivity();

        return mv;
    }
}
