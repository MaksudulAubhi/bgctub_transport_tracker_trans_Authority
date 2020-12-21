package com.example.bgctub_transport_tracker_trans_authority.model;

public class ReportFeedback {
    private String userId;
    private String report_feedback_title;
    private String report_feedback_info;
    private String userEmail;
    private String appNameVersion;
    private String time;
    private String phone_configuration;

    public ReportFeedback(String userId, String report_feedback_title,
                          String report_feedback_info, String userEmail,
                          String appNameVersion, String time,String phone_configuration) {
        this.userId = userId;
        this.report_feedback_title = report_feedback_title;
        this.report_feedback_info = report_feedback_info;
        this.userEmail = userEmail;
        this.appNameVersion = appNameVersion;
        this.time = time;
        this.phone_configuration=phone_configuration;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReport_feedback_title() {
        return report_feedback_title;
    }

    public void setReport_feedback_title(String report_feedback_title) {
        this.report_feedback_title = report_feedback_title;
    }

    public String getReport_feedback_info() {
        return report_feedback_info;
    }

    public void setReport_feedback_info(String report_feedback_info) {
        this.report_feedback_info = report_feedback_info;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAppNameVersion() {
        return appNameVersion;
    }

    public void setAppNameVersion(String appNameVersion) {
        this.appNameVersion = appNameVersion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone_configuration() {
        return phone_configuration;
    }

    public void setPhone_configuration(String phone_configuration) {
        this.phone_configuration = phone_configuration;
    }
}
