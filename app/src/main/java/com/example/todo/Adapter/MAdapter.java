package com.example.todo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Model.ToModel;
import com.example.todo.NotesActivity;
import com.example.todo.R;
import com.example.todo.open;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MAdapter extends RecyclerView.Adapter<MAdapter.MyViewHolder> {

private List<ToModel> toModelList;
private NotesActivity activity;
private FirebaseFirestore firestore;
private FirebaseAuth mAuth;
        String userid;
public MAdapter(NotesActivity notesActivity,List<ToModel> toModelList){
        this.toModelList =toModelList;
        activity = notesActivity;
        }

    @NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newnote,parent,false);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getUid();
        return new MyViewHolder(view);
        }
public void deleteTask(int position){
        ToModel toModel = toModelList.get(position);
        firestore.collection(userid).document(toModel.NoteId).delete();
        toModelList.remove(position);
        notifyItemRemoved(position);
        }
public Context getContext(){
        return activity;
        }
public void editTask(int position){
        ToModel toModel = toModelList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("note",toModel.getNote());
        bundle.putString("id",toModel.NoteId);
        open Openn = new open();
        Openn .setArguments(bundle);
        notifyItemChanged(position);
        }
@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToModel toModel = toModelList.get(position);
        holder.gotit.setText(toModel.getNote());
        holder.gotit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Toast.makeText(getContext(),"yes sir",Toast.LENGTH_SHORT).show();
                        ToModel toModel = toModelList.get(position);

                        Bundle bundle = new Bundle();
                        bundle.putString("note",toModel.getNote());
                        bundle.putString("id",toModel.NoteId);
                        open Openn = new open();
                        Openn .setArguments(bundle);
                        Intent intent1 = new Intent(getContext(),open.class);
                        intent1.putExtra("note",toModel.getNote());
                        intent1.putExtra("id",toModel.NoteId);
                        activity.startActivity(intent1);
                        notifyItemChanged(position);
                }
        });
        holder.gotit.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("are you sure").setTitle("Delete").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteTask(position);
                                }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                        notifyItemChanged(position);
                                }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                }
        });
}
@Override
public int getItemCount() {
        return toModelList.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView gotit;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
            gotit = itemView.findViewById(R.id.gotit);
    }
}
}