package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.MAdapter;
import com.example.todo.Model.ToModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    Button addButton;
    private MAdapter Madapter;
    String userid;
    RecyclerView rView;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<ToModel> toModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        rView = findViewById(R.id.rView);
        addButton = findViewById(R.id.addButton);
        firestore = FirebaseFirestore.getInstance();
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(NotesActivity.this));

        mAuth=FirebaseAuth.getInstance();
        userid=mAuth.getUid();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this,open.class);
                startActivity(intent);
                finish();
            }
        });

        toModelList = new ArrayList<>();

        Madapter = new MAdapter(NotesActivity.this,toModelList);

        showData();
        rView.setAdapter(Madapter);
        Madapter.notifyDataSetChanged();
    }
    private void showData(){
        firestore.collection(userid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                for(DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String idd = documentChange.getDocument().getId();

                        if(documentChange.getDocument().contains("note")){
                            ToModel toModel = documentChange.getDocument().toObject(ToModel.class).withId(idd);
                            toModelList.add(toModel);
                            Madapter.notifyDataSetChanged();
                        }
                        else {
                            Log.i("hello there", idd.toString());
                        }

                    }
                }
            }
        });



//        firestore.collection(userid).document("note").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        String idd = document.getString("note");
//
//                        Madapter.notifyDataSetChanged();
//                        Log.d("hello buddy whatsapp", idd);
//                    }
//                }
//            }
//        });
    }
}