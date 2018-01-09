package com.rameshmklll.church;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;



import java.io.InputStream;
import java.util.Iterator;

public class XlReaderActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnReadExcel1;
    AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xl_reader);

        btnReadExcel1 = (Button) findViewById(R.id.btnReadExcel1);

        btnReadExcel1.setOnClickListener(XlReaderActivity.this);

        assetManager = getAssets();

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnReadExcel1) {

//            readExcelFileFromAssets();

        }

    }

//    public void readExcelFileFromAssets() {
//
//        try {
//            // Creating Input Stream
//   /*
//    * File file = new File( filename); FileInputStream myInput = new
//    * FileInputStream(file);
//    */
//
//            InputStream myInput;
//
//            //  Don't forget to Change to your assets folder excel sheet
//            myInput = assetManager.open("bible.xls");
//
//            // Create a POIFSFileSystem object
//            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
//
//            // Create a workbook using the File System
//            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
//
//            // Get the first sheet from workbook
//            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
//
//            /** We now need something to iterate through the cells. **/
//            Iterator<Row> rowIter = mySheet.rowIterator();
//            rowIter.next();
//
//            while (rowIter.hasNext()) {
//                HSSFRow myRow = (HSSFRow) rowIter.next();
//                Cell cell=myRow.getCell(1);
//                Cell cell_version=myRow.getCell(3);
//                if(cell.getNumericCellValue()==1 && cell_version.getNumericCellValue()==1){
//                    Cell cell_content=myRow.getCell(2);
//
//                    Log.i("chapters", String.valueOf(cell_content.getStringCellValue()));
//                }
//
//                Iterator<Cell> cellIter = myRow.cellIterator();
////                while (cellIter.hasNext()) {
////                    HSSFCell myCell = (HSSFCell) cellIter.next();
////                  Log.i( "cell content", myCell.getStringCellValue()  );
////                    Log.e("FileUtils", "Cell Value: " + myCell.toString()+ " Index :" +myCell.getColumnIndex());
////                    // Toast.makeText(getApplicationContext(), "cell Value: " +
////                    // myCell.toString(), Toast.LENGTH_SHORT).show();
////                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return;
//    }
}
