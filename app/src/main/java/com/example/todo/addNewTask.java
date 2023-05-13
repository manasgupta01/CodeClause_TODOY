package com.example.todo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class addNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";

    private TextView setDueDate;
    private EditText mTaskEdit;
    private Button button;
    private Context context;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    String userid;
    private String dueDate="";
    private String id = "";
    private String dueDateUpdate="";

    public static addNewTask newInstance(){
        return new addNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate = view.findViewById(R.id.set_due_tv);
        mTaskEdit= view.findViewById(R.id.task_editText);
        button=view.findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getUid();
        firestore = FirebaseFirestore.getInstance();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle!=null){
            isUpdate=true;
            String task = bundle.getString("task");
            id = bundle.getString("id");
            dueDateUpdate=bundle.getString("due");
            mTaskEdit.setText(task);
            setDueDate.setText(dueDateUpdate);

            if(task.length()>0){
                button.setEnabled(false);
                button.setBackgroundColor(Color.GRAY);
            }
        }
        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    button.setEnabled(false);
                    button.setBackgroundColor(Color.WHITE);
                    button.setTextColor(Color.GRAY);
                }else {
                    button.setEnabled(true);
                    button.setTextColor(Color.WHITE);
                    button.setBackgroundColor(getResources().getColor(R.color.green_blue));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int MONTH = calendar.get(calendar.MONTH);
                int YEAR= calendar.get(calendar.YEAR);
                int DAY = calendar.get(calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month+1;
                    setDueDate.setText(dayOfMonth+"/"+month+"/"+year);
                    dueDate=dayOfMonth+"/"+month+"/"+year;
                    }
                },YEAR,MONTH,DAY);
                datePickerDialog.show();
            }
        });
         boolean FinalisUpdate = isUpdate;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = mTaskEdit.getText().toString();

                if(FinalisUpdate){

                    firestore.collection(userid).document(id).update("task",task,"due",dueDate);
                    Toast.makeText(context,"Task Updated!!",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (task.isEmpty()) {
                        Toast.makeText(context, "Empty task not Allowed!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> taskMap = new HashMap<>();

                        taskMap.put("task", task);
                        taskMap.put("due", dueDate);
                        taskMap.put("status", 0);
                        taskMap.put("time", FieldValue.serverTimestamp());
                        firestore.collection(userid).add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Task Saved!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context= context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogeCloseListner){
           ((OnDialogeCloseListner) activity).onDialogClose(dialog);
        }
    }
}
