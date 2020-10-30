package com.mobileedu34.schoolhub.callbacks.accounts;

import com.mobileedu34.schoolhub.models.User;

public class AccountsActionController implements ActionContract.Presenter, ActionContract.OnActionResultListener {
    private ActionContract.View mView;
    private ActionCenter mActionCenter;
    private static AccountsActionController mInstance;

    public void setListener(ActionContract.View view) {
        this.mView = view;
        mActionCenter = new ActionCenter(this);
    }

    public AccountsActionController() {}

    public static AccountsActionController getInstance() {
        if(mInstance == null) {
            mInstance = new AccountsActionController();
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
    public void signUpUser(String emailAddress, String password, String fullName) {
        mActionCenter.signUpUser(emailAddress, password, fullName);
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
    public void onSignInUserSuccess(User user) {
        mView.onSignInUserSuccess(user);
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
