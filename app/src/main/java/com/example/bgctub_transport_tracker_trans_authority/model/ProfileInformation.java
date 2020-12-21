package com.example.bgctub_transport_tracker_trans_authority.model;

public class ProfileInformation {
    private String userId;
    private String Name;
    private String Gender;
    private String email;
    private String contact;
    private String office_no;
    private String post;

    public ProfileInformation(String userId, String name, String gender, String email, String contact, String office_no, String post) {
        this.userId = userId;
        Name = name;
        Gender = gender;
        this.email = email;
        this.contact = contact;
        this.office_no = office_no;
        this.post = post;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOffice_no() {
        return office_no;
    }

    public void setOffice_no(String office_no) {
        this.office_no = office_no;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
