package com.example.sqlitedemo;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyRecyclerView>  implements ItemMoveCallback.ItemTouchHelperContract{
    private CustomClickListener customClickListener;
    private List<String[]> customListView;
    View snackbarView;
    private String mRecentlyDeletedItem[];
    private int mRecentlyDeletedItemPosition;
    private Context context;

    public CustomAdapter(List<String[]> customTextView, CustomClickListener customClickListener, Context context) {
        this.customListView = customTextView;
        this.customClickListener = customClickListener;
        this.context = context;
    }
    public void setCustomListView(List<String[]> customListView) {
        this.customListView = customListView;
    }

    @NonNull
    @Override
    public MyRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_items,parent,false);
        MyRecyclerView viewHolder = new MyRecyclerView(itemView);
        snackbarView = parent.getRootView();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customClickListener.onItemClick(itemView, viewHolder.getPosition());
            }
        });
        return  viewHolder;
    }

    @Override
    public void onItemDismiss(int position) {
        String[] deleteIndex = customListView.get(position);
        mRecentlyDeletedItemPosition = position;
        mRecentlyDeletedItem = customListView.get(position);
        customListView.remove(position);
        TaskDBHelper taskDBHelper = new TaskDBHelper(context);
        final Handler handler = new Handler();
        showUndoSnackbar();
        Log.i("Id",deleteIndex[0]);
        taskDBHelper.DeleteFromReminders(deleteIndex[0]);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount()-position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerView holder, int position) {
        String temp[] = customListView.get(position);
        holder.customTextView.setText(temp[1]);
    }

    @Override
    public int getItemCount() {
        return customListView.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        try {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(customListView, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(customListView, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        } catch (Exception ex) {
        }
    }

    @Override
    public void onRowSelected(MyRecyclerView myViewHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myViewHolder.rowView.setBackgroundResource(R.drawable.border);
        }
    }

    @Override
    public void onRowClear(MyRecyclerView myViewHolder) {
        myViewHolder.rowView.setBackgroundResource(R.drawable.border);
    }

    private void undoDelete() {
        customListView.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        TaskDBHelper dbHelper = new TaskDBHelper(context);
        dbHelper.addTask(mRecentlyDeletedItem[1]);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        notifyItemRangeChanged(mRecentlyDeletedItemPosition,getItemCount());
    }

    private void showUndoSnackbar() {
        Snackbar.make(snackbarView, "Task deleted.", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> undoDelete()).show();
    }

    public class MyRecyclerView extends RecyclerView.ViewHolder {
        TextView customTextView;
        View rowView;
        public MyRecyclerView(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            customTextView = itemView.findViewById(R.id.notestextView);
        }
    }
}
