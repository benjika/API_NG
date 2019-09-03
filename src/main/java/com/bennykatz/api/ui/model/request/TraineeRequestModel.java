package com.bennykatz.api.ui.model.request;

public class TraineeRequestModel {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String dateOfBirth;
    private String InstructorId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getInstructorId() {
        return InstructorId;
    }

    public void setInstructorId(String instructorId) {
        InstructorId = instructorId;
    }
}
