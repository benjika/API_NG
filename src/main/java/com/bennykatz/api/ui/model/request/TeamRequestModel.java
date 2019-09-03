package com.bennykatz.api.ui.model.request;

import java.util.List;

public class TeamRequestModel {
    private String name;
    private List<TraineeRequestModel> trainees;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TraineeRequestModel> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<TraineeRequestModel> trainees) {
        this.trainees = trainees;
    }
}
