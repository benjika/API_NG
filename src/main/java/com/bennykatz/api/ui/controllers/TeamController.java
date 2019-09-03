package com.bennykatz.api.ui.controllers;

import com.bennykatz.api.exeptions.UserServiceException;
import com.bennykatz.api.service.TeamService;
import com.bennykatz.api.shared.dto.TeamDto;
import com.bennykatz.api.ui.model.request.TeamRequestModel;
import com.bennykatz.api.ui.model.response.ErrorMessagesEnum;
import com.bennykatz.api.ui.model.response.TeamRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    TeamService teamService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public TeamRest createTeam(@RequestBody TeamRequestModel teamDetails) {

        if (teamDetails.getName().isEmpty()) {
            throw new UserServiceException(ErrorMessagesEnum.MISSING_REQUIRED_FIELD.getErrorMessage());
        }
        TeamDto teamDto = new ModelMapper().map(teamDetails, TeamDto.class);

        if (teamDto.getTrainees() == null) {
            teamDto.setTrainees(new ArrayList<>());
        }

        TeamDto createdTeam = teamService.createTeam(teamDto);

        return new ModelMapper().map(createdTeam, TeamRest.class);
    }

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public TeamRest getTeam(@PathVariable String id) {
        TeamDto teamDto = teamService.getTeamById(id);

        return new ModelMapper().map(teamDto, TeamRest.class);
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<TeamRest> getTeamsList(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<TeamDto> teamDtos = teamService.getTeamsList(page, limit);

        List<TeamRest> returnedValue = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();

        for (TeamDto teamDto : teamDtos) {
            TeamRest teamRest = modelMapper.map(teamDto, TeamRest.class);
            returnedValue.add(teamRest);
        }

        return returnedValue;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public TeamRest updateTeam(@PathVariable String id, @RequestBody TeamRequestModel teamDetails) {
        ModelMapper modelMapper = new ModelMapper();

        TeamDto teamDto = modelMapper.map(teamDetails, TeamDto.class);

        TeamDto updatedTeam = teamService.updateTeam(id, teamDto);

        return modelMapper.map(updatedTeam, TeamRest.class);
    }

    @DeleteMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String deleteTeam() {
        return "deleteTeam called";
    }
}
