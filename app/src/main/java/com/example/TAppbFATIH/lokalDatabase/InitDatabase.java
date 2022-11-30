package com.example.TAppbFATIH.lokalDatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.TAppbFATIH.retrofit.modelResponseData.Data;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;

public class InitDatabase {
    Context context;
    myDbHelper myDbHelper;
    public InitDatabase(Context context){
        myDbHelper = new myDbHelper(context);
    }

    public long insert(Data data){
        SQLiteDatabase dbb = myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", data.getId());
        contentValues.put("nama",data.getNama());
        contentValues.put("password",data.getPassword());
        contentValues.put("login_type",data.getLoginType());
        contentValues.put("user_access",data.getUserAccess());
//        Log.e("data",data.getUserAccess());
        contentValues.put("token",data.getToken());
        long id = dbb.insert("user", null , contentValues);
        return id;
    }
    public int getTotalInit()
    {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        String[] columns = {};
        Cursor cursor =db.query("user",columns,null,null,null,null,null);
        int jumlah = 0;
        while (cursor.moveToNext())
        {
            jumlah++;
        }
        return jumlah;
    }

    public void databaseToCSV(){

        File exportDir = new File(Environment.getExternalStorageDirectory()+"/csvname.csv");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

//        File file = new File(exportDir, "csvname.csv");
        try
        {
            exportDir.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(exportDir));
            SQLiteDatabase db = myDbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM user",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public int delete(String table)
    {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
//        String[] whereArgs ={idUser};
        int count =db.delete( table, null,null);
        return  count;
    }

    @SuppressLint("Range")
    public String getGlobalVariableString(String table, String column){
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        String[] columns = {column};
        Cursor cursor =db.query(table,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        String result = "";
        while (cursor.moveToNext())
        {
            result = cursor.getString(cursor.getColumnIndex(column));
        }
        return result;
    }


    public static class myDbHelper extends SQLiteOpenHelper
    {
        Context context;
        private static String Table_init = "CREATE TABLE user (id INTEGER PRIMARY KEY, nama VARCHAR(255), username VARCHAR(255)," +
                " password VARCHAR(255),login_type VARCHAR(255),user_access VARCHAR(255),token VARCHAR(1000));";

        public myDbHelper(Context context) {
            super(context, "trips", null, 2);
            this.context=context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(Table_init);
            } catch (Exception e) {
                Log.e(String.valueOf(context), "onCreate: "+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            try {
                db.execSQL("DROP TABLE IF EXISTS user");
                onCreate(db);
            }catch (Exception e) {
                Log.e(String.valueOf(context),""+e);
            }
        }
    }
}
