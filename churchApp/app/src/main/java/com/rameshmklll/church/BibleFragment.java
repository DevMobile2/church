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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.rameshmklll.church.adapters.TeluguBibleAdapter;
import com.rameshmklll.church.pojos.TeluguBiblePojo;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link BibleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BibleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ArrayList<TeluguBiblePojo> data;
    private static TeluguBibleAdapter adapter;
    ProgressDialog progress;
    AssetManager assetManager;
    String chapter = "1", version = "1", book_name = "ఆదికాండము";
    int chapter_pos=0, version_pos=0, book_name_pos=0;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    View view;
    boolean match = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//    private HSSFRow myRow;
    SqliteController controller;
    Activity activity;
    TextView tvTitle,tvChapter;
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Bible");
        progress = new ProgressDialog(getActivity());

        //  recyclerView.setHasFixedSize(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bible, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        controller = new SqliteController(getActivity());
        activity = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        tvTitle= (TextView) view.findViewById(R.id.tvTitle);
        tvChapter= (TextView) view.findViewById(R.id.tvChapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));
        data = new ArrayList<TeluguBiblePojo>();
        try {
            controller.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
   new GetDataFromDatabase().execute(book_name, chapter, version);

        adapter = new TeluguBibleAdapter(data, version, activity);
        recyclerView.setAdapter(adapter);
//        new GetDataFromDatabase().execute(book_name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case 2:
                Button btSearch;
                final Dialog dialog = new CustomDialogue(getActivity());
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void readExcelFileFromAssets() {
//
//        try {
//            // Creating Input Stream
//   /*
//    * File file = new File( filename); FileInputStream myInput = new
//    * FileInputStream(file);
//    */SqliteController controller = new SqliteController(getActivity());
//            data = new ArrayList<>();
//            adapter.clearDataSet();
//            InputStream myInput;
//            assetManager = getActivity().getAssets();
//
//            //  Don't forget to Change to your assets folder excel sheet
//            myInput = assetManager.open("telugu_bible.xls");
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
//
//            while (rowIter.hasNext()) {
//                // HSSFRow  HSSFRowmyRow = (HSSFRow) rowIter.next();
//                myRow = (HSSFRow) rowIter.next();
//                Cell cell_book_name = myRow.getCell(0);
//                Cell cell_chapter = myRow.getCell(1);
//                Cell cell_version = myRow.getCell(3);
//                Cell cell_content = myRow.getCell(2);
//                String book_name=String.valueOf(cell_book_name.getStringCellValue());
//                String chapter_Number= String.valueOf(Double.parseDouble(String.valueOf(cell_chapter.getNumericCellValue())));
//                String chapter_version= String.valueOf(Double.parseDouble(String.valueOf(cell_version.getNumericCellValue())));
//                String content= cell_content.getStringCellValue();
//                HashMap<String,String> map=new HashMap<>();
//                map.put("chapter_number",chapter_Number);
//                map.put("book_name",book_name);
//                map.put("version_number",chapter_version);
//                map.put("content",content);
//                controller.insertStudent(map);
//                Log.i("bookame", cell_book_name.getStringCellValue()+"and"+book_name+chapter+"and"+chapter);
//              /*  if (String.valueOf(cell_book_name.getStringCellValue()).equalsIgnoreCase(book_name) *//*&& chapter_Number.equalsIgnoreCase(chapter)*//*) {
//                    Log.i("teluguu", String.valueOf(cell_book_name.getStringCellValue()));
//                    match = true;
//                    Cell cell_content = myRow.getCell(2);
//                    String content = String.valueOf(cell_content.getStringCellValue());
//                    int id = (int) cell_version.getNumericCellValue();
//                    Log.i("chapters", String.valueOf(cell_chapter.getNumericCellValue()));
//                    TeluguBiblePojo teluguBiblePojo = new TeluguBiblePojo(content, id);
//                    data.add(teluguBiblePojo);
//
//                }*/
//
//
//
//
//            //    Iterator<Cell> cellIter = myRow.cellIterator();
////                while (cellIter.hasNext()) {
////                    HSSFCell myCell = (HSSFCell) cellIter.next();
////                  Log.i( "cell content", myCell.getStringCellValue()  );
////                    Log.e("FileUtils", "Cell Value: " + myCell.toString()+ " Index :" +myCell.getColumnIndex());
////                    // Toast.makeText(getApplicationContext(), "cell Value: " +
////                    // myCell.toString(), Toast.LENGTH_SHORT).show();
////                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return;
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 2, 1, "Search").setIcon(R.drawable.ic_search).setShowAsAction(1);
    }

    private void setHeightAndWidth() {
        WindowManager manager = (WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE);
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


    class GetDataFromDatabase extends AsyncTask<String, Void, Void> {
        ProgressDialog progressBar=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMessage("Loading...");
//            progressBar.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            data.clear();
         data=   controller.getStudentInfo(strings[0],version,chapter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.dismiss();
            adapter = new TeluguBibleAdapter(data, version, activity);
            recyclerView.setAdapter(adapter);
            recyclerView.smoothScrollToPosition(version_pos);
            tvTitle.setText(book_name);
            tvChapter.setText("Chapter " +chapter);
        }
    }






    class ReadExcel extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setMessage("Loading...");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
//            readExcelFileFromAssets();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            adapter = new TeluguBibleAdapter(data, version, activity);
            recyclerView.setAdapter(adapter);
        }
    }

    class CustomDialogue extends Dialog {

        Spinner spVersions, spChapters, spBooks;
        Button btSearch;
        ArrayAdapter<String> adapter;
        ArrayList<String> verse_number=new ArrayList<>();
        ArrayList<String> books=new ArrayList<>();
        public CustomDialogue(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.bible_search_layout);
            // setHeightAndWidth();
            spVersions = (Spinner) findViewById(R.id.spVersion);
            spChapters = (Spinner) findViewById(R.id.spChapter);
            spBooks = (Spinner) findViewById(R.id.spBookName);
            try {
                controller.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }

            books=controller.getBookNames();
          adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, books);
            spBooks.setAdapter(adapter);
            btSearch = (Button) findViewById(R.id.btnReadExcel1);


            if (version_pos>=0){
                spVersions.setSelection(version_pos);
            }

            if (chapter_pos>=0){
                spChapters.setSelection(chapter_pos);
            }

            if (book_name_pos>=0){
                spBooks.setSelection(book_name_pos);
            }

            spBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                private Object versesBasedOnBookNameAndChapter;



                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    book_name = adapterView.getSelectedItem().toString();
                    book_name_pos = adapterView.getSelectedItemPosition();
                    getChaptersAccordingToBookName(book_name);
                    getVersesBasedOnBookNameAndChapter();
                }

                private void getChaptersAccordingToBookName(String book_name) {
                     ArrayList<String> chapter_number=   controller.getChapters(book_name);
                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, chapter_number);
                    spChapters.setAdapter(adapter);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            spChapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    chapter=adapterView.getSelectedItem().toString();
                    chapter_pos = adapterView.getSelectedItemPosition();
                    getVersesBasedOnBookNameAndChapter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            spVersions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    version_pos = adapterView.getSelectedItemPosition();
                    version = adapterView.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            btSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   Toast.makeText(getActivity(), book_name + chapter, Toast.LENGTH_SHORT).show();
                    dismiss();
                    new GetDataFromDatabase().execute(book_name,chapter, version);

                }
            });

        }
        public void getVersesBasedOnBookNameAndChapter() {
             verse_number=   controller.getVerses(spBooks.getSelectedItem().toString(),spChapters.getSelectedItem().toString());
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, verse_number);
            spVersions.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
