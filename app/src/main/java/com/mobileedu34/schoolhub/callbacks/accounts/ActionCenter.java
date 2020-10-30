package com.mobileedu34.schoolhub.callbacks.accounts;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileedu34.schoolhub.models.User;


public class ActionCenter implements ActionContract.OnPerformAction {

    private ActionContract.OnActionResultListener mOnActionResultListener;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private boolean creatingAdminAccount = false;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String USERS_COLLECTION = "users";
    private String managerId;

    public ActionCenter(ActionContract.OnActionResultListener onActionResultListener) {
        this.mOnActionResultListener = onActionResultListener;
    }

    @Override
    public void createUser(User user) {

        mDatabase.child(USERS_COLLECTION)
                .child(managerId)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(creatingAdminAccount) {
                            creatingAdminAccount = false;
                            mOnActionResultListener.onSignUpUserSuccess();
                        } else {
                            mOnActionResultListener.onCreateUserSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        creatingAdminAccount = false;
                        mOnActionResultListener.onCreateUserFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void signInUser(final String emailAddress, final String password) {
        final boolean[] isPasswordIncorrect = {false};
        fAuth
                .signInWithEmailAndPassword(emailAddress, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mOnActionResultListener.onSignInUserSuccess(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDatabase.child(USERS_COLLECTION)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
                                            for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                User user = postSnapshot.getValue(User.class);
                                                if(user != null) {
                                                    if(user.getEmailAddress().equals(emailAddress)) {
                                                        if(user.getRandomPassword() != null && user.getRandomPassword().equals(password)) {
                                                            mOnActionResultListener.onSignInUserSuccess(user);
                                                        } else {
                                                            mOnActionResultListener.onSignInUserFailure("Sorry, the password you provided is incorrect.");
                                                            isPasswordIncorrect[0] = true;
                                                        }
                                                        break;
                                                    }
                                                } else {
                                                    mOnActionResultListener.onSignInUserFailure("Sorry, an unexpected error occurred.");
                                                }
                                            }
                                            if(!isPasswordIncorrect[0]) {
                                                mOnActionResultListener.onSignInUserFailure("Sorry, there is no user corresponding to the provided credentials.");
                                            }
                                        } else {
                                            mOnActionResultListener.onSignInUserFailure("Sorry, a network or server error occurred.");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        mOnActionResultListener.onSignInUserFailure("Sorry, a network or server error occurred: " + error.getMessage());
                                    }
                                });
                    }
                });
    }

    @Override
    public void signUpUser(final String emailAddress, String password, final String fullName) {
        fAuth
                .createUserWithEmailAndPassword(emailAddress, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        managerId = fAuth.getCurrentUser().getUid();
                        creatingAdminAccount = true;
                        User manager = new User();
                        manager.setUserId(managerId);
                        manager.setEmailAddress(emailAddress);
                        manager.setUserRole(2);
                        manager.setFullName(fullName);
                        createUser(manager);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onSignUpUserFailure(e.getMessage());
                    }
                });
    }
}

