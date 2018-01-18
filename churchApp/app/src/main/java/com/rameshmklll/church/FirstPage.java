package com.rameshmklll.church;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class FirstPage extends Fragment {
    View mv;
    Activity activity;
    CarouselView carouselView;
    public String TAG = FirstPage.class.getSimpleName();
    int[] sampleImages = {R.drawable.image1, R.drawable.image1, R.drawable.image1, R.drawable.image1};
    private String userName;
    private TextView wish_id,tvEvenining,tvMorning,tvDate,tvDay;
    SqliteController controller;
    ImageView iv_profile;

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
        wish_id=mv.findViewById(R.id.wish_id);
        tvDate = mv.findViewById(R.id.tvDate);
        tvDay = mv.findViewById(R.id.tvDay);
        iv_profile=mv.findViewById(R.id.iv_profile);
        carouselView.setPageCount(sampleImages.length);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("CSI Christ church");
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
            setImage();
            getCurrentTime();
            setAlmanicDetails();
    }

    private void setImage() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String photo_url=pref.getString("photo_url",null);// 0 - for private mode
        setProfilePic(photo_url);
    }



    private void setProfilePic(String mPhotoUrl) {
        Picasso.with(activity)
                .load(mPhotoUrl)
//                .resize(60, 50)
//                .centerCrop()
                .into(iv_profile, new Callback() {
                    @Override
                    public void onSuccess() {
                        //code
                    }
                    @Override
                    public void onError() {
                        //code
                    }
                });
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

        byte[] sha1 = {
                (byte)0xB2, 0x60, 0x78, 0x4B, (byte)0xBE, 0x6B, (byte)0xD2, 0x4D, 0x43, 0x38,     (byte)0xFB, 0x6A, 0x2F, 0x52, 0x00, 0x61, 0x3E, 0x34, 0x5E, 0x74
        };
        Log.e("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));
        Calendar calendar= Calendar.getInstance();
        Date dat = calendar.getTime();
//        Log.d(TAG, dat+"");
       String[] list = dat.toString().split(" ");
       String mnth = list[1];
       String date = list[2];
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTimeInMillis());
        String weekday_dtae= new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH).format(calendar.getTimeInMillis());
        tvDate.setText(weekday_dtae+"");
        tvDay.setText(weekday_name+"");
       String mnth_date = mnth+date;

//       Log.d(TAG, mnth+", "+date);

        Time time = new Time(System.currentTimeMillis());
        int hours = time.getHours();
         String wish_message;
//        Log.d(TAG, hours+", ");
        if (hours<12){
            wish_message="Good Morning";
        }else if (hours>=12 && hours<5){
            wish_message="Good AfterNoon";

        }else {
            wish_message="Good Evening";

        }

        if (mnth_date.equalsIgnoreCase("Dec25")){
            wish_message="Happy Christmas";

        }else if (mnth_date.equalsIgnoreCase("Jan01")){
            wish_message="Happy NewYear";

        }else if (mnth_date.equalsIgnoreCase("Dec19")){
            wish_message="Happy BirthDay";

        }
        wish_id.setText(wish_message+" "+userName);

    }

}
