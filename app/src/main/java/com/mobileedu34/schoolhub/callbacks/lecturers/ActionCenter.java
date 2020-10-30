package com.mobileedu34.schoolhub.callbacks.lecturers;

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
    private ArrayList<User> lecturers;

    public ActionCenter(ActionContract.OnActionResultListener onActionResultListener) {
        this.mOnActionResultListener = onActionResultListener;
    }

    //2xt74n30
    //test1@test.com

    @Override
    public void createLecturer(User lecturer) {
        mDatabase.child(USERS_COLLECTION)
                .child(lecturer.getUserId())
                .setValue(lecturer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onCreateLecturerSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onCreateLecturerFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void updateLecturer(User lecturer) {
        mDatabase.child(USERS_COLLECTION)
                .child(lecturer.getUserId())
                .setValue(lecturer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onUpdateLecturerSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onUpdateLecturerFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void deleteLecturer(String lecturerId) {
        mDatabase
                .child(USERS_COLLECTION)
                .child(lecturerId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onDeleteLecturerSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onDeleteLecturerFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void getLecturers() {
        lecturers = new ArrayList<>();
        mDatabase.child(USERS_COLLECTION)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                User user = postSnapshot.getValue(User.class);
                                if(user != null && user.getUserRole() == 1) {
                                    lecturers.add(user);
                                }
                            }
                            mOnActionResultListener.onGetLecturersSuccess(lecturers);
                        } else {
                            mOnActionResultListener.onGetLecturersFailure(snapshot.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mOnActionResultListener.onGetLecturersFailure("Could not retrieve lecturers : " + error.getMessage());
                    }
                });
    }
}

