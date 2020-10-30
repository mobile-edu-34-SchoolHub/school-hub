package com.mobileedu34.schoolhub.callbacks.classrooms;

import com.mobileedu34.schoolhub.models.Classroom;

import java.util.ArrayList;

public class ClassroomsActionController implements ActionContract.Presenter, ActionContract.OnActionResultListener {
    private ActionContract.View mView;
    private ActionCenter mActionCenter;
    private static ClassroomsActionController mInstance;

    public void setListener(ActionContract.View view) {
        this.mView = view;
        mActionCenter = new ActionCenter(this);
    }

    public ClassroomsActionController() {}

    public static ClassroomsActionController getInstance() {
        if(mInstance == null) {
            mInstance = new ClassroomsActionController();
        }
        return mInstance;
    }

    @Override
    public void createClassroom(Classroom classroom) {
        mActionCenter.createClassroom(classroom);
    }

    @Override
    public void updateClassroom(Classroom classroom) {
        mActionCenter.updateClassroom(classroom);
    }

    @Override
    public void deleteClassroom(String classroomId) {
        mActionCenter.deleteClassroom(classroomId);
    }

    @Override
    public void getClassrooms() {
        mActionCenter.getClassrooms();
    }

    @Override
    public void onCreateClassroomSuccess() {
        mView.onCreateClassroomSuccess();
    }

    @Override
    public void onCreateClassroomFailure(String message) {
        mView.onCreateClassroomFailure(message);
    }

    @Override
    public void onUpdateClassroomSuccess() {
        mView.onUpdateClassroomSuccess();
    }

    @Override
    public void onUpdateClassroomFailure(String message) {
        mView.onUpdateClassroomFailure(message);
    }

    @Override
    public void onDeleteClassroomSuccess() {
        mView.onDeleteClassroomSuccess();
    }

    @Override
    public void onDeleteClassroomFailure(String message) {
        mView.onDeleteClassroomFailure(message);
    }

    @Override
    public void onGetClassroomsSuccess(ArrayList<Classroom> classrooms) {
        mView.onGetClassroomsSuccess(classrooms);
    }

    @Override
    public void onGetClassroomsFailure(String message) {
        mView.onGetClassroomsFailure(message);
    }
}
