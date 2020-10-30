package com.mobileedu34.schoolhub.models;

public class LectureExtras {

    private String id;
    private String docName;
    private String docDownloadUrl;
    private String courseId;

    public LectureExtras() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocDownloadUrl() {
        return docDownloadUrl;
    }

    public void setDocDownloadUrl(String docDownloadUrl) {
        this.docDownloadUrl = docDownloadUrl;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
