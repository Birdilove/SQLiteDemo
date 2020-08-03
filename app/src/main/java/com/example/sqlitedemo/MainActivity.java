package com.example.sqlitedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements StartDragListener{
    StaggeredGridLayoutManager staggaggeredGridLayoutManager;
    private List<String[]> taskList;
    public TaskDBHelper taskDBHelper;
    private ItemTouchHelper itemTouchHelper;
    private CustomAdapter adapter;
    private RecyclerView recyclerViewItems;
    private static final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskDBHelper = new TaskDBHelper(this);
        taskList = taskDBHelper.browseReminders();
        staggaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        adapter = new CustomAdapter(taskList, (v, position) -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent addTask1 = new Intent(MainActivity.this, AddTask.class);
            String[] temp = taskList.get(position);
            addTask1.putExtra("clickedPosition", temp[0]);
            startActivityForResult(addTask1,1);
        },this);
        recyclerViewItems = findViewById(R.id.rv2);
        recyclerViewItems.setLayoutManager(staggaggeredGridLayoutManager);
        recyclerViewItems.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerViewItems);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent addTask = new Intent(this, AddTask.class);
            startActivityForResult(addTask,1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, 1, data);
        taskList = taskDBHelper.browseReminders();
        adapter.setCustomListView(taskList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
