package com.example.todo.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class NoteId {
    @Exclude
    public String NoteId;
    public <N extends NoteId> N withId(@NonNull final String idd){
        this.NoteId=idd;
        return  (N) this;
    }
}
