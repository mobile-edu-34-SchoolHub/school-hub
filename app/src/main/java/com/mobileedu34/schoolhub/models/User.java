package com.mobileedu34.schoolhub.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mobileedu34.schoolhub.helpers.UserRole;

public class User implements Parcelable {

    private String userId;
    private int userRole;
    private String fullName;
    private String emailAddress;
    private String randomPassword;
    private String photoUrl;
    private String phoneNumber;
    private String gender;
    private String classroomId;

    public User() {
    }

    public User(Parcel in) {
        this.userId = in.readString();
        this.classroomId = in.readString();
        this.userRole = in.readInt();
        this.fullName = in.readString();
        this.gender = in.readString();
        this.phoneNumber = in.readString();
        this.emailAddress = in.readString();
        this.randomPassword = in.readString();
        this.photoUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(classroomId);
        dest.writeInt(userRole);
        dest.writeString(fullName);
        dest.writeString(gender);
        dest.writeString(emailAddress);
        dest.writeString(phoneNumber);
        dest.writeString(randomPassword);
        dest.writeString(photoUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRandomPassword() {
        return randomPassword;
    }

    public void setRandomPassword(String randomPassword) {
        this.randomPassword = randomPassword;
    }

    public int getUserRole() {
        return userRole;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public static UserRole getUserRole(int role) {
        UserRole userRole = null;
        switch (role) {
            case 0:
                userRole = UserRole.STUDENT;
                break;
            case 1:
                userRole = UserRole.LECTURER;
                break;
            case 2:
                userRole = UserRole.SCHOOL_MANAGER;
                break;
        }
        return userRole;
    }

}
