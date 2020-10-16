package com.mobileedu34.schoolhub.callbacks.users;

import com.mobileedu34.schoolhub.models.User;

public class UsersActionController implements ActionContract.Presenter, ActionContract.OnActionResultListener {
    private ActionContract.View mView;
    private ActionCenter mActionCenter;
    private static UsersActionController mInstance;

    public void setListener(ActionContract.View view) {
        this.mView = view;
        mActionCenter = new ActionCenter(this);
    }

    public UsersActionController() {}

    public static UsersActionController getInstance() {
        if(mInstance == null) {
            mInstance = new UsersActionController();
        }
        return mInstance;
    }

    @Override
    public void createUser(User user) {
        mActionCenter.createUser(user);
    }

    @Override
    public void signInUser(String emailAddress, String password) {
        mActionCenter.signInUser(emailAddress, password);
    }

    @Override
    public void signUpUser(String emailAddress, String password) {
        mActionCenter.signUpUser(emailAddress, password);
    }

    @Override
    public void onCreateUserSuccess() {
        mView.onCreateUserSuccess();
    }

    @Override
    public void onCreateUserFailure(String message) {
        mView.onCreateUserFailure(message);
    }

    @Override
    public void onSignInUserSuccess() {
        mView.onSignInUserSuccess();
    }

    @Override
    public void onSignInUserFailure(String message) {
        mView.onSignInUserFailure(message);
    }

    @Override
    public void onSignUpUserSuccess() {
        mView.onSignUpUserSuccess();
    }

    @Override
    public void onSignUpUserFailure(String message) {
        mView.onSignUpUserFailure(message);
    }
}
