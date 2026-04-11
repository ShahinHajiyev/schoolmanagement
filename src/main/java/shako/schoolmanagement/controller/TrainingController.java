package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.dto.TrainingDto;
import shako.schoolmanagement.service.inter.TrainingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @GetMapping("/my")
    public ResponseEntity<TrainingDto> getMyTraining() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String neptunCode = auth.getName();
        log.info("GET /training/my — student: {}", neptunCode);
        TrainingDto training = trainingService.getMyTraining(neptunCode);
        if (training == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(training);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TrainingDto> getAllTrainings() {
        log.info("GET /training/all");
        return trainingService.getAllTrainings();
    }
}
