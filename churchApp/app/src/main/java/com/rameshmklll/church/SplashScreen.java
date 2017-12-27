package com.rameshmklll.church;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rameshmklll.church.adapters.TeluguBibleAdapter;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
            insertBibleInDatabase();
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

            readExcelFileFromAssets();
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



    public void readExcelFileFromAssets() {

        try {
            // Creating Input Stream
   /*
    * File file = new File( filename); FileInputStream myInput = new
    * FileInputStream(file);
    */SqliteController sqliteController=new SqliteController(this);
            AssetManager assetManager=getAssets();
            InputStream myInput;
            assetManager=getAssets();

            //  Don't forget to Change to your assets folder excel sheet
            myInput = assetManager.open("AlmanacFin.xls");

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFRow myRow;
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

    public void insertBibleInDatabase() {

        try {
            // Creating Input Stream
   /*
    * File file = new File( filename); FileInputStream myInput = new
    * FileInputStream(file);
    */SqliteController controller = new SqliteController(this);

            InputStream myInput;
         AssetManager assetManager = this.getAssets();

            //  Don't forget to Change to your assets folder excel sheet
            myInput = assetManager.open("telugu_bible.xls");

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
    HSSFRow myRow;
            /** We now need something to iterate through the cells. **/
            Iterator<Row> rowIter = mySheet.rowIterator();
            rowIter.next();


            while (rowIter.hasNext()) {
                // HSSFRow  HSSFRowmyRow = (HSSFRow) rowIter.next();
                myRow = (HSSFRow) rowIter.next();
                Cell cell_book_name = myRow.getCell(0);
                Cell cell_chapter = myRow.getCell(1);
                Cell cell_version = myRow.getCell(3);
                Cell cell_content = myRow.getCell(2);
                String book_name=String.valueOf(cell_book_name.getStringCellValue());
                String chapter_Number= String.valueOf(Double.parseDouble(String.valueOf(cell_chapter.getNumericCellValue())));
                String chapter_version= String.valueOf(Double.parseDouble(String.valueOf(cell_version.getNumericCellValue())));
                String content= cell_content.getStringCellValue();
                HashMap<String,String> map=new HashMap<>();
                map.put("chapter_number",chapter_Number);
                map.put("book_name",book_name);
                map.put("version_number",chapter_version);
                map.put("content",content);
                controller.insertStudent(map);
              //  Log.i("bookame", cell_book_name.getStringCellValue()+"and"+book_name+chapter+"and"+chapter);
              /*  if (String.valueOf(cell_book_name.getStringCellValue()).equalsIgnoreCase(book_name) *//*&& chapter_Number.equalsIgnoreCase(chapter)*//*) {
                    Log.i("teluguu", String.valueOf(cell_book_name.getStringCellValue()));
                    match = true;
                    Cell cell_content = myRow.getCell(2);
                    String content = String.valueOf(cell_content.getStringCellValue());
                    int id = (int) cell_version.getNumericCellValue();
                    Log.i("chapters", String.valueOf(cell_chapter.getNumericCellValue()));
                    TeluguBiblePojo teluguBiblePojo = new TeluguBiblePojo(content, id);
                    data.add(teluguBiblePojo);

                }*/




                //    Iterator<Cell> cellIter = myRow.cellIterator();
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
