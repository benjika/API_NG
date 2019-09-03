package com.bennykatz.api.io.repositories;

import com.bennykatz.api.io.entity.TeamEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends PagingAndSortingRepository<TeamEntity, Long> {
    TeamEntity findByName(String name);

    TeamEntity findByTeamId(String name);
}
