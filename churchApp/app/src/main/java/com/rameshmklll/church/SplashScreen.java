package com.rameshmklll.church;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
     new ReadExcel().execute();
/*        Intent intent=new Intent(SplashScreen.this,MainActivity.class);
        startActivity(intent);*/
    }





    class ReadExcel extends AsyncTask<String, Void, Void> {
        ProgressDialog progress=new ProgressDialog(SplashScreen.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setMessage("Loading bible...");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            new ReadAlmanic().execute();
        }
    }

    class ReadAlmanic extends AsyncTask<String,Void,Void> {
        ProgressDialog progress=new ProgressDialog(SplashScreen.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setMessage("Loading almanic");
            progress.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            Intent intent=new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intent);

        }
    }





    String getFormattedDate(String input){

        DateFormat inputFormat = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

        String newPattern = "EEE MMM dd HH:mm:ss Z yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(newPattern);
        formatter.setLenient(true);
        Date date = null;
        try {
            date = formatter.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy",
                Locale.ENGLISH);
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String output = outputFormat.format(date);
        System.out.println(output);
        return  output;
    }
}
