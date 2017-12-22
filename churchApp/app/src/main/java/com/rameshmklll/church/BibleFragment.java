package com.rameshmklll.church;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.rameshmklll.church.adapters.TeluguBibleAdapter;
import com.rameshmklll.church.pojos.TeluguBiblePojo;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link BibleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BibleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressDialog progress;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AssetManager assetManager;
    private HSSFRow myRow;
    String chapter="2.0",version="1",book_name="ఆదికాండము";
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    View view;
    private static ArrayList<TeluguBiblePojo> data;
    private static TeluguBibleAdapter adapter;
    boolean match=false;

    public BibleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BibleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BibleFragment newInstance(String param1, String param2) {
        BibleFragment fragment = new BibleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Bible");
          progress=new ProgressDialog(getActivity());

      //  recyclerView.setHasFixedSize(true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_bible, container, false);

        return  view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView)view. findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),layoutManager.getOrientation()));
        data = new ArrayList<TeluguBiblePojo>();
        adapter = new TeluguBibleAdapter(data);
        recyclerView.setAdapter(adapter);

       new ReadExcel().execute();


    }


   class ReadExcel extends AsyncTask<String,Void,Void>{

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
           adapter=new TeluguBibleAdapter(data);
           recyclerView.setAdapter(adapter);
       }
   }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id){
            case 2:
                Button btSearch;
                final Dialog dialog = new CustomDialogue(getActivity());
                dialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }



































    public void readExcelFileFromAssets() {

        try {
            // Creating Input Stream
   /*
    * File file = new File( filename); FileInputStream myInput = new
    * FileInputStream(file);
    */
         data=new ArrayList<>();
            InputStream myInput;
            assetManager=getActivity().getAssets();

            //  Don't forget to Change to your assets folder excel sheet
            myInput = assetManager.open("telugu_bible.xls");

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
                Cell cell_book_name=myRow.getCell(0);
                Cell cell_chapter=myRow.getCell(1);
                Cell cell_version=myRow.getCell(3);

                if( String.valueOf(cell_book_name.getStringCellValue()).equalsIgnoreCase(book_name)  &&  String.valueOf(cell_chapter.getNumericCellValue()).equalsIgnoreCase(chapter) ){
                    Log.i("teluguu",String.valueOf(cell_book_name.getStringCellValue()));
                    match=true;
                    Cell cell_content=myRow.getCell(2);
                    String content=String.valueOf(cell_content.getStringCellValue());
                    int id= (int) cell_version.getNumericCellValue();
                    Log.i("chapters", String.valueOf(cell_chapter.getNumericCellValue()));
                    TeluguBiblePojo teluguBiblePojo=new TeluguBiblePojo(content,id);
                    data.add(teluguBiblePojo);
                }
                else{
                    if(match){
                        break;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0,2,1,"Search").setIcon(R.drawable.ic_search).setShowAsAction(1);
    }

    private void setHeightAndWidth() {
        WindowManager manager = (WindowManager)getActivity().getSystemService(Activity.WINDOW_SERVICE);
        int width, height;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            width = point.x;
            height = point.y;
        }
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getActivity().getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        getActivity().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    class CustomDialogue extends Dialog{

     Spinner spVersions,spChapters,spBooks;
        Button btSearch;
        public CustomDialogue(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.bible_search_layout);
           // setHeightAndWidth();
            spVersions=(Spinner) findViewById(R.id.spVersion);
            spChapters=(Spinner) findViewById(R.id.spChapter);
            spBooks=(Spinner) findViewById(R.id.spBookName);
            btSearch=(Button) findViewById(R.id.btnReadExcel1);
            btSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    book_name=spBooks.getSelectedItem().toString();
                    version=spVersions.getSelectedItem().toString();
                    Toast.makeText(getActivity(), book_name+version, Toast.LENGTH_SHORT).show();
                    dismiss();
                    readExcelFileFromAssets();
                }
            });
        }
    }
}
