package com.mobileedu34.schoolhub.callbacks.classrooms;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileedu34.schoolhub.models.Classroom;
import java.util.ArrayList;
import static com.mobileedu34.schoolhub.helpers.Constants.CLASSROOMS_COLLECTION;


public class ActionCenter implements ActionContract.OnPerformAction {

    private ActionContract.OnActionResultListener mOnActionResultListener;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Classroom> classrooms;

    public ActionCenter(ActionContract.OnActionResultListener onActionResultListener) {
        this.mOnActionResultListener = onActionResultListener;
    }

    //2xt74n30
    //test1@test.com

    @Override
    public void createClassroom(Classroom classroom) {
        mDatabase.child(CLASSROOMS_COLLECTION)
                .child(classroom.getId())
                .setValue(classroom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onCreateClassroomSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onCreateClassroomFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void updateClassroom(Classroom classroom) {
        mDatabase.child(CLASSROOMS_COLLECTION)
                .child(classroom.getId())
                .setValue(classroom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onUpdateClassroomSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onUpdateClassroomFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void deleteClassroom(String classroomId) {
        mDatabase
                .child(CLASSROOMS_COLLECTION)
                .child(classroomId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onDeleteClassroomSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onDeleteClassroomFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void getClassrooms() {
        classrooms = new ArrayList<>();
        mDatabase.child(CLASSROOMS_COLLECTION)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Classroom classroom = postSnapshot.getValue(Classroom.class);
                                classrooms.add(classroom);
                            }
                            mOnActionResultListener.onGetClassroomsSuccess(classrooms);
                        } else {
                            mOnActionResultListener.onGetClassroomsFailure(snapshot.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mOnActionResultListener.onGetClassroomsFailure("Could not retrieve classrooms : " + error.getMessage());
                    }
                });
    }
}

