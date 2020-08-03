package com.example.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TaskDBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public TaskDBHelper(@Nullable Context context) {
        super(context, TaskContract.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTasksTableCmd = "CREATE TABLE tasks" +
                "( " + TaskContract.TaskEntry._ID + " INTEGER Primary key AUTOINCREMENT NOT NULL, " +
                TaskContract.TaskEntry.TASK_TEXT + " TEXT);";
        db.execSQL(createTasksTableCmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE ADD_NAME ADD COLUMN NEW_COLOUM INTEGER DEFAULT 0");
        }
        onCreate(db);
    }

    public boolean addTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task);
        db.insert("tasks", null, contentValues);
        return true;
    }

    public String searchReminders(String id){
        String searchResult = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor =  db.rawQuery( "select * from "+ TaskContract.TaskEntry.TABLE_NAME +"  WHERE "+TaskContract.TaskEntry._ID+" =  \"" + id + "\"", null );
            if (cursor != null){
                cursor.moveToFirst();
                searchResult = cursor.getString(1);
            }
        } catch (Exception ex) {
            Log.e("Browse Reminders ", ex.getMessage());
        }
        return  searchResult;
    }

    public List<String[]> browseReminders(){
        List<String[]> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor =  db.rawQuery( "select * from tasks", null );
            if (cursor != null){
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    String[] eachTask = new String[2];
                    eachTask[0] = cursor.getString(0);
                    eachTask[1] = cursor.getString(1);
                    tasks.add(eachTask);
                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {
            Log.e("Browse Reminders ", ex.getMessage());
        }
        return tasks;
    }

    public void UpdateTasks(String id, String task){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.TASK_TEXT,task);
            db.update(TaskContract.TaskEntry.TABLE_NAME, values,TaskContract.TaskEntry._ID + " = ? ",new String[]{ String.valueOf(id) });
        }
        catch (Exception e){
            Log.d("Update Tasks: ",e.getMessage());
        }
    }

    public void DeleteFromReminders(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String where="_id=?";
            db.delete("tasks", where, new String[]{id});
        }
        catch(Exception e){
            Log.d("DeleteFromReminders", e.getMessage());
        }
    }

}
