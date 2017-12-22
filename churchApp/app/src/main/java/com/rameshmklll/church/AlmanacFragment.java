package com.rameshmklll.church;


import android.app.DatePickerDialog;
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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            date=day+"/"+month+"/"+year;
            readExcelFileFromAssets();
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_almanac, container, false);

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

    }


    class ReadExcel extends AsyncTask<String,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress.setMessage("Loading");
//            progress.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            readExcelFileFromAssets();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void readExcelFileFromAssets() {

        try {
            // Creating Input Stream
   /*
    * File file = new File( filename); FileInputStream myInput = new
    * FileInputStream(file);
    */
            AssetManager assetManager=getActivity().getAssets();
            InputStream myInput;
            assetManager=getActivity().getAssets();

            //  Don't forget to Change to your assets folder excel sheet
            myInput = assetManager.open("Almanac.xls");

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

                if(unformattedDate!="null") {
                    String date_new =getFormattedDate(String.valueOf(cell_date.getDateCellValue()));
                   // Log.i("cell_datessss", getFormattedDate(date));
                    Log.i("date1",date);
                    Log.i("date2",date_new);
                    if( date_new.equalsIgnoreCase(date)   ){


                        String morning_content=String.valueOf(cell_morning.getStringCellValue());
                        String evening_content= String.valueOf(cell_evening .getStringCellValue());
                        Log.i("content1", morning_content);
                        Log.i("content2", evening_content);
                    }
                    else{

                    }
                }



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
