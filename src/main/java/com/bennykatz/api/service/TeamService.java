package com.bennykatz.api.service;

import com.bennykatz.api.shared.dto.TeamDto;

import java.util.List;

public interface TeamService {
    TeamDto createTeam(TeamDto teamDto);

    TeamDto getTeamById(String teamId);

    List<TeamDto> getTeamsList(int page, int limit);

    TeamDto updateTeam(String teamId, TeamDto teamDto);
}
