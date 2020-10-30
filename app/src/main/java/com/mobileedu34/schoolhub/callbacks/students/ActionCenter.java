package com.mobileedu34.schoolhub.callbacks.students;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileedu34.schoolhub.models.User;

import java.util.ArrayList;


public class ActionCenter implements ActionContract.OnPerformAction {

    private ActionContract.OnActionResultListener mOnActionResultListener;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser fUser = fAuth.getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String USERS_COLLECTION = "users";
    private String managerId;
    private ArrayList<User> students;

    public ActionCenter(ActionContract.OnActionResultListener onActionResultListener) {
        this.mOnActionResultListener = onActionResultListener;
    }


    @Override
    public void createStudent(User student) {
        mDatabase.child(USERS_COLLECTION)
                .child(student.getUserId())
                .setValue(student)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onCreateStudentSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onCreateStudentFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void updateStudent(User student) {
        mDatabase.child(USERS_COLLECTION)
                .child(student.getUserId())
                .setValue(student)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onUpdateStudentSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onUpdateStudentFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void deleteStudent(String studentId) {
        mDatabase
                .child(USERS_COLLECTION)
                .child(studentId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onDeleteStudentSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onDeleteStudentFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void getStudents() {
        students = new ArrayList<>();
        mDatabase.child(USERS_COLLECTION)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                User user = postSnapshot.getValue(User.class);
                                if(user != null && user.getUserRole() == 0) {
                                    students.add(user);
                                }
                            }
                            mOnActionResultListener.onGetStudentsSuccess(students);
                        } else {
                            mOnActionResultListener.onGetStudentsFailure(snapshot.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mOnActionResultListener.onGetStudentsFailure("Could not retrieve students : " + error.getMessage());
                    }
                });
    }
}

