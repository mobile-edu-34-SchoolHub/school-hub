package com.mobileedu34.schoolhub.callbacks.accounts;

import com.mobileedu34.schoolhub.models.User;

public interface ActionContract {
    interface View {
        void onCreateUserSuccess();
        void onCreateUserFailure(String message);
        void onSignInUserSuccess(User user);
        void onSignInUserFailure(String message);
        void onSignUpUserSuccess();
        void onSignUpUserFailure(String message);
    }

    interface Presenter {
        void createUser(User user);
        void signInUser(String emailAddress, String password);
        void signUpUser(String emailAddress, String password, String fullName);
    }

    interface OnPerformAction {
        void createUser(User user);
        void signInUser(String emailAddress, String password);
        void signUpUser(String emailAddress, String password, String fullName);
    }

    interface OnActionResultListener {
        void onCreateUserSuccess();
        void onCreateUserFailure(String message);
        void onSignInUserSuccess(User user);
        void onSignInUserFailure(String message);
        void onSignUpUserSuccess();
        void onSignUpUserFailure(String message);
    }
}
