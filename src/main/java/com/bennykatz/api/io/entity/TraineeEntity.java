package com.bennykatz.api.io.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "trainees")
public class TraineeEntity implements Serializable {

    private static final long serialVersionUID = -5522133975201743741L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 30)
    private String firstName;

    @Column(nullable = false, length = 30)
    private String lastName;
    @Column(nullable = false, length = 13, unique = true)
    private String phoneNumber;

    @Column(length = 10)
    private String dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "instructors_id")
    private InstructorEntity instructor;

    @ManyToOne
    @JoinColumn(name = "teams_id")
    private TeamEntity team;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public InstructorEntity getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorEntity instructor) {
        this.instructor = instructor;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }
}
