package com.mobileedu34.schoolhub.callbacks.users;

import com.mobileedu34.schoolhub.models.User;

public interface ActionContract {
    interface View {
        void onCreateUserSuccess();
        void onCreateUserFailure(String message);
        void onSignInUserSuccess();
        void onSignInUserFailure(String message);
        void onSignUpUserSuccess();
        void onSignUpUserFailure(String message);
    }

    interface Presenter {
        void createUser(User user);
        void signInUser(String emailAddress, String password);
        void signUpUser(String emailAddress, String password);
    }

    interface OnPerformAction {
        void createUser(User user);
        void signInUser(String emailAddress, String password);
        void signUpUser(String emailAddress, String password);
    }

    interface OnActionResultListener {
        void onCreateUserSuccess();
        void onCreateUserFailure(String message);
        void onSignInUserSuccess();
        void onSignInUserFailure(String message);
        void onSignUpUserSuccess();
        void onSignUpUserFailure(String message);
    }
}
