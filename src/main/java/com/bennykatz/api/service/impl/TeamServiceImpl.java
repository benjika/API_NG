package com.bennykatz.api.service.impl;

import com.bennykatz.api.exeptions.UserServiceException;
import com.bennykatz.api.io.entity.TeamEntity;
import com.bennykatz.api.io.repositories.TeamRepository;
import com.bennykatz.api.service.TeamService;
import com.bennykatz.api.shared.Utils;
import com.bennykatz.api.shared.dto.TeamDto;
import com.bennykatz.api.ui.model.response.ErrorMessagesEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    Utils utils;

    @Override
    public TeamDto createTeam(TeamDto teamDto) {


        if (teamRepository.findByName(teamDto.getName()) != null) {
            throw new RuntimeException("Record already exists");
        }

        TeamEntity teamEntity = new ModelMapper().map(teamDto, TeamEntity.class);
        teamEntity.setTeamId(utils.generateTeamId(30));

        TeamEntity storedTeam = teamRepository.save(teamEntity);

        TeamDto returnedValue = new TeamDto();
        BeanUtils.copyProperties(storedTeam, returnedValue);
        return returnedValue;
    }

    @Override
    public TeamDto getTeamById(String teamId) {
        TeamEntity teamEntity = teamRepository.findByTeamId(teamId);

        if (teamEntity == null) {
            throw new UserServiceException(ErrorMessagesEnum.TEAM_NOT_FOUND.getErrorMessage());
        }

        return new ModelMapper().map(teamEntity, TeamDto.class);
    }

    @Override
    public List<TeamDto> getTeamsList(int page, int limit) {
        List<TeamDto> returnedValue = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, limit);
        Page<TeamEntity> teamEntityPage = teamRepository.findAll(pageable);

        List<TeamEntity> teamEntities = teamEntityPage.getContent();

        ModelMapper modelMapper = new ModelMapper();

        for (TeamEntity teamEntity : teamEntities) {
            TeamDto teamDto = modelMapper.map(teamEntity, TeamDto.class);
            returnedValue.add(teamDto);
        }

        return returnedValue;
    }

    @Override
    public TeamDto updateTeam(String teamId, TeamDto teamDto) {
        TeamEntity teamEntity = teamRepository.findByTeamId(teamId);

        if (teamEntity == null) {
            throw new UserServiceException(ErrorMessagesEnum.TEAM_NOT_FOUND.getErrorMessage());
        }

        if (!teamDto.getName().isEmpty()) {
            teamEntity.setName(teamDto.getName());
        }

        TeamEntity updatedTeamEntity = teamRepository.save(teamEntity);

        return new ModelMapper().map(updatedTeamEntity, TeamDto.class);
    }
}
