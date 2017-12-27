package com.rameshmklll.church;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by MRamesh on 19-12-2017.
 */

public class AlmanacFragment extends Fragment {

    View view;
    Button btDate;
    private int year, month, day;
    private HSSFRow myRow;
    String date;
    TextView tvMorning,tvEvenining;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            year = arg1;
            month = arg2+1;
            day = arg3;
            String newdate,newmonth;
            if(day<10)
                newdate="0"+day;
            else
                newdate= String.valueOf(day);
            if(month<10)
                newmonth="0"+month;
            else
                newmonth= String.valueOf(month);
            date=newdate+"/"+newmonth+"/"+year;
           // readExcelFileFromAssets();
            getData(date);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_almanac, container, false);
        tvEvenining=view.findViewById(R.id.tvEvening);
        tvMorning=view.findViewById(R.id.tvMorning);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        btDate=(Button) view.findViewById(R.id.btDatepicker);
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDialog = new DatePickerDialog(getActivity(), myDateListener,
                        year, month, day);
                mDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 20000);
                mDialog.show();
            }
        });

        new ReadExcel().execute();

    }

    public void getData(String date) {
        SqliteController sqliteController=new SqliteController(getActivity());
        HashMap<String,String> data=sqliteController.getAlmanic(date);
       tvMorning.setText("Morning"+"/n/n"+data.get("mornning_content"));
        tvEvenining.setText("Evening"+"/n/n"+data.get("evening_content"));
        btDate.setText(data.get("date"));
      //  Log.i("dataaaaa",data.get("mornning_content"));
    }


    class ReadExcel extends AsyncTask<String,Void,Void> {
        ProgressDialog progress=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progress.setMessage("Loading");
           progress.show();

        }

        @Override
        protected Void doInBackground(String... strings) {
            readExcelFileFromAssets();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();

        }
    }



    public void readExcelFileFromAssets() {

        try {
            // Creating Input Stream
   /*
    * File file = new File( filename); FileInputStream myInput = new
    * FileInputStream(file);
    */SqliteController sqliteController=new SqliteController(getActivity());
            AssetManager assetManager=getActivity().getAssets();
            InputStream myInput;
            assetManager=getActivity().getAssets();

            //  Don't forget to Change to your assets folder excel sheet
            myInput = assetManager.open("AlmanacFinal.xls");

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells. **/
            Iterator<Row> rowIter = mySheet.rowIterator();
            rowIter.next();



            while (rowIter.hasNext()) {
                // HSSFRow  HSSFRowmyRow = (HSSFRow) rowIter.next();
                myRow = (HSSFRow) rowIter.next();
                Cell cell_date=myRow.getCell(0);
                Cell cell_morning=myRow.getCell(2);
                Cell cell_evening=myRow.getCell(3);
                String unformattedDate=String.valueOf(cell_date.getDateCellValue());
                String morning_content=String.valueOf(cell_morning.getStringCellValue());
                String evening_content= String.valueOf(cell_evening .getStringCellValue());
                String date_new =getFormattedDate(String.valueOf(cell_date.getDateCellValue()));
                HashMap<String,String> map=new HashMap<>();
                map.put("date",date_new);
                map.put("morning_content",morning_content);
                map.put("evening_content",evening_content);
                sqliteController.insertAlmanic(map);
//                if(unformattedDate!="null") {
//                    String date_new =getFormattedDate(String.valueOf(cell_date.getDateCellValue()));
//
//                   // Log.i("cell_datessss", getFormattedDate(date));
//                    Log.i("date1",date);
//                    Log.i("date2",date_new);
//                    if( date_new.equalsIgnoreCase(date)   ){
//
//
//                        String morning_content=String.valueOf(cell_morning.getStringCellValue());
//                        String evening_content= String.valueOf(cell_evening .getStringCellValue());
//                        Log.i("content1", morning_content);
//                        Log.i("content2", evening_content);
//                    }
//                    else{
//
//                    }
//                }



                Iterator<Cell> cellIter = myRow.cellIterator();
//                while (cellIter.hasNext()) {
//                    HSSFCell myCell = (HSSFCell) cellIter.next();
//                  Log.i( "cell content", myCell.getStringCellValue()  );
//                    Log.e("FileUtils", "Cell Value: " + myCell.toString()+ " Index :" +myCell.getColumnIndex());
//                    // Toast.makeText(getApplicationContext(), "cell Value: " +
//                    // myCell.toString(), Toast.LENGTH_SHORT).show();
//                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
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
