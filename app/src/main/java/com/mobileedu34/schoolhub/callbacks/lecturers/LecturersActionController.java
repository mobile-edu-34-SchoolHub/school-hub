package com.mobileedu34.schoolhub.callbacks.lecturers;

import com.mobileedu34.schoolhub.models.User;

import java.util.ArrayList;

public class LecturersActionController implements ActionContract.Presenter, ActionContract.OnActionResultListener {
    private ActionContract.View mView;
    private ActionCenter mActionCenter;
    private static LecturersActionController mInstance;

    public void setListener(ActionContract.View view) {
        this.mView = view;
        mActionCenter = new ActionCenter(this);
    }

    public LecturersActionController() {}

    public static LecturersActionController getInstance() {
        if(mInstance == null) {
            mInstance = new LecturersActionController();
        }
        return mInstance;
    }

    @Override
    public void createLecturer(User lecturer) {
        mActionCenter.createLecturer(lecturer);
    }

    @Override
    public void updateLecturer(User lecturer) {
        mActionCenter.updateLecturer(lecturer);
    }

    @Override
    public void deleteLecturer(String lecturerId) {
        mActionCenter.deleteLecturer(lecturerId);
    }

    @Override
    public void getLecturers() {
        mActionCenter.getLecturers();
    }

    @Override
    public void onCreateLecturerSuccess() {
        mView.onCreateLecturerSuccess();
    }

    @Override
    public void onCreateLecturerFailure(String message) {
        mView.onCreateLecturerFailure(message);
    }

    @Override
    public void onUpdateLecturerSuccess() {
        mView.onUpdateLecturerSuccess();
    }

    @Override
    public void onUpdateLecturerFailure(String message) {
        mView.onUpdateLecturerFailure(message);
    }

    @Override
    public void onDeleteLecturerSuccess() {
        mView.onDeleteLecturerSuccess();
    }

    @Override
    public void onDeleteLecturerFailure(String message) {
        mView.onDeleteLecturerFailure(message);
    }

    @Override
    public void onGetLecturersSuccess(ArrayList<User> lecturers) {
        mView.onGetLecturersSuccess(lecturers);
    }

    @Override
    public void onGetLecturersFailure(String message) {
        mView.onGetLecturersFailure(message);
    }
}
