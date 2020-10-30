package com.mobileedu34.schoolhub.callbacks.classrooms;

import com.mobileedu34.schoolhub.models.Classroom;

import java.util.ArrayList;

public interface ActionContract {
    interface View {
        void onCreateClassroomSuccess();
        void onCreateClassroomFailure(String message);
        void onUpdateClassroomSuccess();
        void onUpdateClassroomFailure(String message);
        void onDeleteClassroomSuccess();
        void onDeleteClassroomFailure(String message);
        void onGetClassroomsSuccess(ArrayList<Classroom> classrooms);
        void onGetClassroomsFailure(String message);
    }

    interface Presenter {
        void createClassroom(Classroom classroom);
        void updateClassroom(Classroom classroom);
        void deleteClassroom(String classroomId);
        void getClassrooms();
    }

    interface OnPerformAction {
        void createClassroom(Classroom classroom);
        void updateClassroom(Classroom classroom);
        void deleteClassroom(String classroomId);
        void getClassrooms();
    }

    interface OnActionResultListener {
        void onCreateClassroomSuccess();
        void onCreateClassroomFailure(String message);
        void onUpdateClassroomSuccess();
        void onUpdateClassroomFailure(String message);
        void onDeleteClassroomSuccess();
        void onDeleteClassroomFailure(String message);
        void onGetClassroomsSuccess(ArrayList<Classroom> classrooms);
        void onGetClassroomsFailure(String message);
    }
}
