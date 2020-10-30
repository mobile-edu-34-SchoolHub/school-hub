package com.mobileedu34.schoolhub.callbacks.lecturers;

import com.mobileedu34.schoolhub.models.User;

import java.util.ArrayList;

public interface ActionContract {
    interface View {
        void onCreateLecturerSuccess();
        void onCreateLecturerFailure(String message);
        void onUpdateLecturerSuccess();
        void onUpdateLecturerFailure(String message);
        void onDeleteLecturerSuccess();
        void onDeleteLecturerFailure(String message);
        void onGetLecturersSuccess(ArrayList<User> lecturers);
        void onGetLecturersFailure(String message);
    }

    interface Presenter {
        void createLecturer(User lecturer);
        void updateLecturer(User lecturer);
        void deleteLecturer(String lecturerId);
        void getLecturers();
    }

    interface OnPerformAction {
        void createLecturer(User lecturer);
        void updateLecturer(User lecturer);
        void deleteLecturer(String lecturerId);
        void getLecturers();
    }

    interface OnActionResultListener {
        void onCreateLecturerSuccess();
        void onCreateLecturerFailure(String message);
        void onUpdateLecturerSuccess();
        void onUpdateLecturerFailure(String message);
        void onDeleteLecturerSuccess();
        void onDeleteLecturerFailure(String message);
        void onGetLecturersSuccess(ArrayList<User> lecturers);
        void onGetLecturersFailure(String message);
    }
}
