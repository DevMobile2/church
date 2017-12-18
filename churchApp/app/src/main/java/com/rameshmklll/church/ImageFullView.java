package com.rameshmklll.church;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

/**
 * Created by venkatesh on 15/12/17.
 */

public class ImageFullView extends AppCompatActivity {

    private CarouselView carouselView;
    int[] sampleImages = {R.drawable.mt, R.drawable.mt, R.drawable.mt, R.drawable.mt, R.drawable.mt,
            R.drawable.mt, R.drawable.mt, R.drawable.mt, R.drawable.mt, R.drawable.mt};
    private int pos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_fullview_layout);
        pos = getIntent().getIntExtra("pos",-1);

        carouselView = findViewById(R.id.carousel_view);

        carouselView.setPageCount(sampleImages.length);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };
        carouselView.setImageListener(imageListener);

    }
}
