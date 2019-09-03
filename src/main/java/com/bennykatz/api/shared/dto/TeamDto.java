package com.bennykatz.api.shared.dto;

import java.io.Serializable;
import java.util.List;

public class TeamDto implements Serializable {

    private static final long serialVersionUID = -4808146531382709140L;
    private String TeamId;
    private String name;
    private List<TraineeDto> trainees;

    public String getTeamId() {
        return TeamId;
    }

    public void setTeamId(String teamId) {
        TeamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TraineeDto> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<TraineeDto> trainees) {
        this.trainees = trainees;
    }
}
