package com.bennykatz.api.io.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "teams")
public class TeamEntity implements Serializable {

    private static final long serialVersionUID = -2167598566816191637L;

    @Id
    @GeneratedValue
    private long Id;

    @Column(nullable = false, length = 30)
    private String teamId;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @OneToMany(mappedBy = "trainees_id", cascade = CascadeType.ALL)
    private List<TraineeEntity> trainee;

    @ManyToOne

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
