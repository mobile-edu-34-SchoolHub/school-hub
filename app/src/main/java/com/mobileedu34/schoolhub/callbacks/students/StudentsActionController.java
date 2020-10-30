package com.mobileedu34.schoolhub.callbacks.students;

import com.mobileedu34.schoolhub.models.User;

import java.util.ArrayList;

public class StudentsActionController implements ActionContract.Presenter, ActionContract.OnActionResultListener {
    private ActionContract.View mView;
    private ActionCenter mActionCenter;
    private static StudentsActionController mInstance;

    public void setListener(ActionContract.View view) {
        this.mView = view;
        mActionCenter = new ActionCenter(this);
    }

    public StudentsActionController() {}

    public static StudentsActionController getInstance() {
        if(mInstance == null) {
            mInstance = new StudentsActionController();
        }
        return mInstance;
    }

    @Override
    public void createStudent(User student) {
        mActionCenter.createStudent(student);
    }

    @Override
    public void updateStudent(User student) {
        mActionCenter.updateStudent(student);
    }

    @Override
    public void deleteStudent(String studentId) {
        mActionCenter.deleteStudent(studentId);
    }

    @Override
    public void getStudents() {
        mActionCenter.getStudents();
    }

    @Override
    public void onCreateStudentSuccess() {
        mView.onCreateStudentSuccess();
    }

    @Override
    public void onCreateStudentFailure(String message) {
        mView.onCreateStudentFailure(message);
    }

    @Override
    public void onUpdateStudentSuccess() {
        mView.onUpdateStudentSuccess();
    }

    @Override
    public void onUpdateStudentFailure(String message) {
        mView.onUpdateStudentFailure(message);
    }

    @Override
    public void onDeleteStudentSuccess() {
        mView.onDeleteStudentSuccess();
    }

    @Override
    public void onDeleteStudentFailure(String message) {
        mView.onDeleteStudentFailure(message);
    }

    @Override
    public void onGetStudentsSuccess(ArrayList<User> students) {
        mView.onGetStudentsSuccess(students);
    }

    @Override
    public void onGetStudentsFailure(String message) {
        mView.onGetStudentsFailure(message);
    }
}
