package com.mobileedu34.schoolhub.callbacks.students;

import com.mobileedu34.schoolhub.models.User;

import java.util.ArrayList;

public interface ActionContract {
    interface View {
        void onCreateStudentSuccess();
        void onCreateStudentFailure(String message);
        void onUpdateStudentSuccess();
        void onUpdateStudentFailure(String message);
        void onDeleteStudentSuccess();
        void onDeleteStudentFailure(String message);
        void onGetStudentsSuccess(ArrayList<User> students);
        void onGetStudentsFailure(String message);
    }

    interface Presenter {
        void createStudent(User Student);
        void updateStudent(User Student);
        void deleteStudent(String StudentId);
        void getStudents();
    }

    interface OnPerformAction {
        void createStudent(User Student);
        void updateStudent(User Student);
        void deleteStudent(String StudentId);
        void getStudents();
    }

    interface OnActionResultListener {
        void onCreateStudentSuccess();
        void onCreateStudentFailure(String message);
        void onUpdateStudentSuccess();
        void onUpdateStudentFailure(String message);
        void onDeleteStudentSuccess();
        void onDeleteStudentFailure(String message);
        void onGetStudentsSuccess(ArrayList<User> Students);
        void onGetStudentsFailure(String message);
    }
}
