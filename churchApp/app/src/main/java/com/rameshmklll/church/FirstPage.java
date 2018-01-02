package com.rameshmklll.church;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class FirstPage extends Fragment {
    View mv;
    Activity activity;
    CarouselView carouselView;
    public String TAG = FirstPage.class.getSimpleName();
    int[] sampleImages = {R.drawable.image1, R.drawable.image1, R.drawable.image1, R.drawable.image1};
    private String userName;
    private TextView wish_id,tvEvenining,tvMorning;
    SqliteController controller;

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
        tvEvenining = mv.findViewById(R.id.tvEvening);
        tvMorning = mv.findViewById(R.id.tvMorning);
        carouselView.setPageCount(sampleImages.length);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };
        carouselView.setImageListener(imageListener);
        activity = getActivity();
         controller=new SqliteController(getActivity());
        return mv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            TextView name = mv.findViewById(R.id.tv_user);
            wish_id = mv.findViewById(R.id.wish_id);
            if(userName.equalsIgnoreCase("anonymous")){
                name.setVisibility(View.GONE);
            }
            else {
                name.setText(userName);
            }
            getCurrentTime();
            setAlmanicDetails();
    }

    private void setAlmanicDetails() {
        try {

            controller.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getData(DateGetter.getDate());
    }


    public void getData(String date) {
        SqliteController sqliteController = new SqliteController(getActivity());
        HashMap<String, String> data = sqliteController.getAlmanic(date);
        tvMorning.setText(data.get("mornning_content"));
        tvEvenining.setText( data.get("evening_content"));

        //  Log.i("dataaaaa",data.get("mornning_content"));
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
