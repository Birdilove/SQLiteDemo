package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddTask extends AppCompatActivity {
    EditText tasktext;
    Button save;
    public TaskDBHelper taskDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskDBHelper = new TaskDBHelper(this);
        tasktext = findViewById(R.id.editText);
        save = findViewById(R.id.button);
        String getViwOrUpdateIntent = getIntent().getStringExtra("clickedPosition");
        tasktext.setText(taskDBHelper.searchReminders(getViwOrUpdateIntent));
        if(getViwOrUpdateIntent!=null){
            save.setOnClickListener(v -> {
                taskDBHelper.UpdateTasks(getViwOrUpdateIntent,String.valueOf(tasktext.getText()));
                finish();
            });
        }
        else{
            save.setOnClickListener(v -> {
                taskDBHelper.addTask(String.valueOf(tasktext.getText()));
                finish();
            });
        }

    }
}