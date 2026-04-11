package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.TrainingDto;

import java.util.List;

public interface TrainingService {

    TrainingDto getMyTraining(String neptunCode);

    List<TrainingDto> getAllTrainings();
}
