package com.mobileedu34.schoolhub.models;

import com.mobileedu34.schoolhub.helpers.UserRole;

public class User {

    private String userId;
    private int userRole;
    private String fullName;
    private String emailAddress;
    private String randomPassword;
    private String photoUrl;

    public User() {
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
