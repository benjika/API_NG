package com.bennykatz.api.io.repositories;

import com.bennykatz.api.io.entity.TraineeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TraineeRepository extends PagingAndSortingRepository<TraineeEntity, Long> {
    TraineeEntity findByPhoneNumber(String phoneNumber);
}
