package com.example.sqlitedemo;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "TasksDB";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String TASK_TEXT = "title";

    }
}
