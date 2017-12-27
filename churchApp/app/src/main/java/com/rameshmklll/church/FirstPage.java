package com.rameshmklll.church;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.sql.Time;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;


public class FirstPage extends Fragment {
    View mv;
    Activity activity;
    CarouselView carouselView;
    public String TAG = FirstPage.class.getSimpleName();
    int[] sampleImages = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};
    private String userName;
    private TextView wish_id;

    public FirstPage() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            userName =  getArguments().getString("userName");
        }
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            TextView name = mv.findViewById(R.id.tv_user);
            wish_id = mv.findViewById(R.id.wish_id);
            name.setText(userName);
            getCurrentTime();
    }

    private void getCurrentTime() {
        Calendar calendar= Calendar.getInstance();
        Date dat = calendar.getTime();
//        Log.d(TAG, dat+"");

       String[] list = dat.toString().split(" ");
       String mnth = list[1];
       String date = list[2];

       String mnth_date = mnth+date;

//       Log.d(TAG, mnth+", "+date);

        Time time = new Time(System.currentTimeMillis());
        int hours = time.getHours();

//        Log.d(TAG, hours+", ");
        if (hours<12){
            wish_id.setText("Good Morning!");
        }else if (hours>=12 && hours<5){
            wish_id.setText("Good AfterNoon!");
        }else {
            wish_id.setText("Good Evening!");
        }

        if (mnth_date.equalsIgnoreCase("Dec25")){
            wish_id.setText("Happy Christmas!");
        }else if (mnth_date.equalsIgnoreCase("Jan01")){
            wish_id.setText("Happy NewYear!");
        }else if (mnth_date.equalsIgnoreCase("Dec19")){
            wish_id.setText("Happy BirthDay!");
        }

    }

}
