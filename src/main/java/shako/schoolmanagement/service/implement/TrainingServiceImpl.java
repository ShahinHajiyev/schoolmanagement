package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.TrainingDto;
import shako.schoolmanagement.entity.Training;
import shako.schoolmanagement.repository.TrainingRepository;
import shako.schoolmanagement.service.inter.TrainingService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;

    @Override
    public TrainingDto getMyTraining(String neptunCode) {
        log.debug("Fetching training for student: {}", neptunCode);
        return trainingRepository.findByStudent_NeptunCode(neptunCode)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public List<TrainingDto> getAllTrainings() {
        log.debug("Fetching all trainings");
        return trainingRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TrainingDto toDto(Training t) {
        return new TrainingDto(
                t.getTrainingId(),
                t.getNumberOfTerms(),
                t.getModule(),
                t.getStatus(),
                t.getOrganization() != null ? t.getOrganization().getOrganizationName() : null,
                t.getLanguage() != null ? t.getLanguage().getLanguageName() : null,
                t.getProgram() != null ? t.getProgram().getProgramName() : null,
                t.getStudent() != null ? t.getStudent().getNeptunCode() : null
        );
    }
}
