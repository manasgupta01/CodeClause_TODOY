package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class open extends AppCompatActivity {
    private String id = "";
    EditText editText;
    Button saveButton;
    String a="",userid;
    FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        editText=findViewById(R.id.editText);
        mAuth=FirebaseAuth.getInstance();
        userid=mAuth.getUid();
        firestore = FirebaseFirestore.getInstance();
        boolean isUpdate = false;



        Intent intent = getIntent();

        if(intent.hasExtra("note")){
            isUpdate=true;
            String note = intent.getExtras().getString("note");
            id = intent.getExtras().getString("id");
            editText.setText(note);

            if(note.length()==0){
                saveButton.setEnabled(false);
                saveButton.setBackgroundColor(Color.GRAY);
            }

        }
        boolean FinalisUpdate = isUpdate;
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a= editText.getText().toString();

                if(FinalisUpdate){
                    if (a.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Empty task not Allowed!!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        firestore.collection(userid).document(id).update("note", a);
                        Toast.makeText(getApplicationContext(), "Task Updated!!", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    if (a.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Empty task not Allowed!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("note", a);
                        firestore.collection(userid).add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> note) {
                                if (note.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Task Saved!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), note.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                Intent intenttt = new Intent(getApplicationContext(),NotesActivity.class);
                startActivity(intenttt);
                finish();
            }

        });



    }

    private Bundle getArguments() {
        this.setArguments(arguments);
        return null;
    }


    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

}