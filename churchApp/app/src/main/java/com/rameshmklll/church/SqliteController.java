package com.rameshmklll.church;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rameshmklll.church.pojos.TeluguBiblePojo;

public class SqliteController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    Context context;

    public SqliteController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
        context=applicationcontext;
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query1,query2;
        query1 = "CREATE TABLE IF NOT EXISTS Telugu_Bible( chapter_number INTEGER , book_name TEXT,content TEXT,version_number INTEGER)";
        query2 = "CREATE TABLE IF NOT EXISTS Almanic(date TEXT,morning_content TEXT,evening_content TEXT)";
        database.execSQL(query1);
        database.execSQL(query2);
        Log.d(LOGCAT, "Bible Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Students";
        database.execSQL(query);
        onCreate(database);
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
        String path="/data/data/"+context.getPackageName()+"/databases/";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path+"androidsqlite",null,SQLiteDatabase.OPEN_READONLY);//this.getReadableDatabase();
        String selectQuery = "select DISTINCT book_name from  Telugu_Bible" ;
        Cursor cursor=database.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                book_names.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return  book_names;
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
        return  data;
    }


}

