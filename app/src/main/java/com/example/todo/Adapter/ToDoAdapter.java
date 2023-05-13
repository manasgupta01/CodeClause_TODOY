package com.example.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo.MainActivity;
import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.addNewTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> toDoModelList;
    private MainActivity activity;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    ToDoAdapter toDoAdapter;
    String userid;
    public ToDoAdapter(MainActivity mainActivity,List<ToDoModel> toDoModelList){
        this.toDoModelList =toDoModelList;
        activity = mainActivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.each_task,parent,false);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getUid();
        return new MyViewHolder(view);
    }
    public void deleteTask(int position){
        ToDoModel toDoModel = toDoModelList.get(position);
        firestore.collection(userid).document(toDoModel.TaskId).delete();
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }

    public void editTask(int position){
        ToDoModel toDoModel = toDoModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("task",toDoModel.getTask());
        bundle.putString("due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);
        addNewTask AddNewTask = new addNewTask();
        AddNewTask.setArguments(bundle);
        AddNewTask.show(activity.getSupportFragmentManager(),AddNewTask.getTag());
        notifyItemChanged(position);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = toDoModelList.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());
        holder.mDueDateTv.setText("Due on"+ toDoModel.getDue());
        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    firestore.collection(userid).document(toDoModel.TaskId).update("status",1);
                }else{
                firestore.collection(userid).document(toDoModel.TaskId).update("status",0 ) ;
                }
            }
        });
    }

    private boolean toBoolean(int status){
        return status!=0;
    }
    @Override
    public int getItemCount() {
        return toDoModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mDueDateTv;
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.dueDate);
            mCheckBox = itemView.findViewById(R.id.mcheck_box);
        }
    }
}
