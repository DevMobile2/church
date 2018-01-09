package com.rameshmklll.church;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rameshmklll.church.pojos.TeluguBiblePojo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SqliteController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    private static  String DB_PATH = "";
    private static  String DB_NAME = "androidsqlitefinal.db";
    Context context;



    public SqliteController(Context applicationcontext) {
        super(applicationcontext, DB_NAME, null, 2);
        context = applicationcontext;
        Log.d(LOGCAT, "Created");
        DB_PATH = context.getDatabasePath(DB_NAME).toString();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query1,query2;
//        query1 = "CREATE TABLE IF NOT EXISTS Telugu_Bible( chapter_number INTEGER , book_name TEXT,content TEXT,version_number INTEGER)";
//        query2 = "CREATE TABLE IF NOT EXISTS Almanic(date TEXT,morning_content TEXT,evening_content TEXT)";
//        database.execSQL(query1);
//        database.execSQL(query2);
        Log.d(LOGCAT, "Bible Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
//        query = "DROP TABLE IF EXISTS Students";
//        database.execSQL(query);
      //  onCreate(database);
    }

    public void insertStudent(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("chapter_number", queryValues.get("chapter_number"));
        values.put("book_name", queryValues.get("book_name"));
        values.put("version_number", queryValues.get("version_number"));
        values.put("content",queryValues.get("content"));
        database.insert("Telugu_Bible", null, values);
        database.close();
    }
    public void insertAlmanic(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", queryValues.get("date"));
        values.put("morning_content", queryValues.get("morning_content"));
        values.put("evening_content", queryValues.get("evening_content"));
        database.insert("Almanic", null, values);
        database.close();
    }
    public int updateStudent(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StudentName", queryValues.get("StudentName"));
        return database.update("Students", values, "StudentId" + " = ?", new String[]{queryValues.get("StudentId")}); //String updateQuery = "Update words set txtWord='"+word+"' where txtWord='"+ oldWord +"'"; //Log.d(LOGCAT,updateQuery); //database.rawQuery(updateQuery, null); //return database.update("words", values, "txtWord = ?", new String[] { word }); } public void deleteStudent(String id) { Log.d(LOGCAT,"delete"); SQLiteDatabase database = this.getWritableDatabase();	String deleteQuery = "DELETE FROM Students where StudentId='"+ id +"'"; Log.d("query",deleteQuery);	database.execSQL(deleteQuery); } public ArrayList<HashMap<String, String>> getAllStudents() { ArrayList<HashMap<String, String>> wordList; wordList = new ArrayList<HashMap<String, String>>(); String selectQuery = "SELECT * FROM Students"; SQLiteDatabase database = this.getWritableDatabase(); Cursor cursor = database.rawQuery(selectQuery, null); if (cursor.moveToFirst()) { do { HashMap<String, String> map = new HashMap<String, String>(); map.put("StudentId", cursor.getString(0)); map.put("StudentName", cursor.getString(1)); wordList.add(map); } while (cursor.moveToNext()); } // return contact list return wordList; } public HashMap<String, String> getStudentInfo(String id) { HashMap<String, String> wordList = new HashMap<String, String>(); SQLiteDatabase database = this.getReadableDatabase(); String selectQuery = "SELECT * FROM Students where StudentId='"+id+"'"; Cursor cursor = database.rawQuery(selectQuery, null); if (cursor.moveToFirst()) { do { //HashMap<String, String> map = new HashMap<String, String>(); wordList.put("StudentName", cursor.getString(1)); //wordList.add(map); } while (cursor.moveToNext()); }	return wordList; }	}
    }

    public ArrayList<HashMap<String, String>> getAllStudents() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM book_name";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("StudentId", cursor.getString(0));
                map.put("StudentName", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        } // return contact list return wordList; }

        return wordList;
    }


    public ArrayList<TeluguBiblePojo> getStudentInfo(String book_name,String version,String chapter) {
        HashMap<String, String> wordList = new HashMap<String, String>();
        ArrayList<TeluguBiblePojo> data=new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        //String selectQuery = "SELECT * FROM Telugu_Bible where book_name='" + book_name + "' " ;
        String selectQuery = "SELECT * FROM Telugu_Bible where book_name= '"+book_name+"' and chapter_number = '" +chapter+ "' ";
        Cursor cursor = database.rawQuery(selectQuery, null);
        Log.i("length", String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {
            do { //HashMap<String, String> map = new HashMap<String, String>();
                wordList.put("StudentName", cursor.getString(2));
                TeluguBiblePojo data2=new TeluguBiblePojo(cursor.getString(2),cursor.getInt(3));
                data.add(data2);
                // wordList.add(map);
            } while (cursor.moveToNext());
        }
        return data;
    }

    public ArrayList<String> getBookNames(){
        ArrayList<String> book_names=new ArrayList<>();


//        SQLiteDatabase database = SQLiteDatabase.openDatabase(path+"androidsqlitenew",null, SQLiteDatabase.OPEN_READONLY);//this.getReadableDatabase();
        SQLiteDatabase database = getDatabase();


        String selectQuery = "select DISTINCT book_name from Telugu_Bible" ;
        Cursor cursor=database.rawQuery(selectQuery,null);
        Log.d("Db_length: ", cursor.getCount()+"" + "Hi");
        if(cursor.moveToFirst()){
            do {
                book_names.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        database.close();
        return  book_names;
    }

    public SQLiteDatabase getDatabase(){

        String path="/data/data/"+context.getPackageName()+"/databases/";
        String myPath = DB_PATH;

//        SQLiteDatabase database = SQLiteDatabase.openDatabase(path+"androidsqlitenew",null, SQLiteDatabase.OPEN_READONLY);//this.getReadableDatabase();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(myPath,null, SQLiteDatabase.OPEN_READONLY);
        return database;

    }
    public HashMap<String,String> getAlmanic(String date){
        HashMap<String,String> data=new HashMap<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "select * from  Almanic where date= '"+date+"'" ;
        Cursor cursor=database.rawQuery(selectQuery,null);
        Log.i("lllll", String.valueOf(cursor.getCount()));
        if(cursor.moveToFirst()){
            do {
                Log.i("lllll11",cursor.getString(1));
               // book_names.add(cursor.getString(0));
                data.put("date",cursor.getString(0));
                data.put("mornning_content",cursor.getString(1));
                data.put("evening_content",cursor.getString(2));
            }
            while (cursor.moveToNext());
        }
        database.close();
        return  data;
    }


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getWritableDatabase();

            copyDataBase();

        }

    }

    private void copyDataBase() {

        //Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = context.getAssets().open(DB_NAME);
            // Path to the just created empty db
            String outFileName = DB_PATH ;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkDataBase() {

        //  this.getReadableDatabase();

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH ;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){
            checkDB.close();

        }

        return checkDB != null ? true : false;
    }


    public ArrayList<String> getChapters(String book_name) {
        ArrayList<String> chapter_numbers=new ArrayList<>();


//        SQLiteDatabase database = SQLiteDatabase.openDatabase(path+"androidsqlitenew",null, SQLiteDatabase.OPEN_READONLY);//this.getReadableDatabase();
        SQLiteDatabase database = getDatabase();
        String selectQuery = "select DISTINCT chapter_number from Telugu_Bible where book_name='"+book_name+"'" ;
        Cursor cursor=database.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                chapter_numbers.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        database.close();
        return  chapter_numbers;


    }

    public ArrayList<String> getVerses(String book_name, Object chapter_number) {
        ArrayList<String> verse_numbers=new ArrayList<>();


//        SQLiteDatabase database = SQLiteDatabase.openDatabase(path+"androidsqlitenew",null, SQLiteDatabase.OPEN_READONLY);//this.getReadableDatabase();
        SQLiteDatabase database = getDatabase();
        String selectQuery = "select DISTINCT version_number from Telugu_Bible where book_name='"+book_name+"' and chapter_number='"+chapter_number+"' " ;
        Cursor cursor=database.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                verse_numbers.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        database.close();
        return  verse_numbers;

    }
}

