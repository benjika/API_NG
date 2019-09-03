package com.bennykatz.api.ui.controllers;

import com.bennykatz.api.exeptions.UserServiceException;
import com.bennykatz.api.service.TraineeService;
import com.bennykatz.api.shared.dto.TraineeDto;
import com.bennykatz.api.ui.model.request.TraineeRequestModel;
import com.bennykatz.api.ui.model.response.ErrorMessagesEnum;
import com.bennykatz.api.ui.model.response.TraineeRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainees")
public class TraineeController {

    @Autowired
    TraineeService traineeService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public TraineeRest createTrainee(@RequestBody TraineeRequestModel traineeDetails) {

        if (traineeDetails.getFirstName().isEmpty() || traineeDetails.getLastName().isEmpty()) {
            throw new UserServiceException(ErrorMessagesEnum.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        TraineeDto traineeDto = modelMapper.map(traineeDetails, TraineeDto.class);

       /* TraineeDto savedTraineeDto = traineeService.createTrainee(traineeDto);

        return modelMapper.map(savedTraineeDto, TraineeRest.class);*/
        return null;
    }
}
