package com.mobileedu34.schoolhub.callbacks.users;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.mobileedu34.schoolhub.helpers.FirebaseHelper;
import com.mobileedu34.schoolhub.models.User;


public class ActionCenter implements ActionContract.OnPerformAction {

    private ActionContract.OnActionResultListener mOnActionResultListener;

    public ActionCenter(ActionContract.OnActionResultListener onActionResultListener) {
        this.mOnActionResultListener = onActionResultListener;
    }

    @Override
    public void createUser(User user) {
        FirebaseHelper.createUser(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnActionResultListener.onCreateUserSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onCreateUserFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void signInUser(String emailAddress, String password) {
        FirebaseHelper.signInUser(emailAddress, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mOnActionResultListener.onSignInUserSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnActionResultListener.onSignInUserFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void signUpUser(String emailAddress, String password) {
        FirebaseHelper.signUpUser(emailAddress, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mOnActionResultListener.onSignUpUserSuccess();
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

