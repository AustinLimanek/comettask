package com.task.CometTask.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.task.CometTask.R;
import com.task.CometTask.activities.MainActivity;
import com.task.CometTask.activities.TaskDetail;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {

    List<Task> tasks;
    Context callingActivity;

    public TaskRecyclerViewAdapter(List<Task> tasks, Context callingActivity) {
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO Step 1-7: (In RecyclerViewAdapter.onCreateViewHolder()) Inflate fragment
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent, false);
        // TODO Step 1-9: (In RecyclerViewAdapter.onCreateViewHolder()) Attach Fragment to ViewHolder
        return new TaskViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // TODO Step 2-4: (In RecyclerViewAdapter.onBindViewHolder()) Bind data items to Fragments inside of ViewHolders
        TextView taskFragmentTextViewName = holder.itemView.findViewById(R.id.TaskFragmentTextView);
        String mainTask = tasks.get(position).getTaskTitle();
        taskFragmentTextViewName.setText(position+1 + ". " + mainTask);
        // TODO Step 3-3: (In RecyclerViewAdapter.onBindViewHolder()) Create OnClickListener, make an Intent inside it, and call this Intent with an Extra to go to another Activity
        View taskItemView = holder.itemView;
        taskItemView.setOnClickListener(v -> {
            Intent goToTaskDetailIntent = new Intent(callingActivity, TaskDetail.class);
            goToTaskDetailIntent.putExtra(MainActivity.TASK_EXTRA_TAG, mainTask);
            String mainTaskId = tasks.get(position).getId();
            goToTaskDetailIntent.putExtra(MainActivity.TASK_EXTRA_ID, mainTaskId);
            callingActivity.startActivity(goToTaskDetailIntent);
        });
    }

    @Override
    public int getItemCount() {
        // TODO Step 1-10: (In RecyclerViewAdapter.getItemCount()) For testing purposes, hardcode a large number of items
//    return 100;
        // TODO Step 2-5: (In RecyclerViewAdapter.getItemCount()) Make the size of the list dynamic
        return tasks.size();
    }

    // TODO Step 1-8: (In RecyclerViewAdapter) Make a ViewHolder class to hold a Fragment
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
