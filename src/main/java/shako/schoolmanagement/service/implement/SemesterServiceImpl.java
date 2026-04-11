package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.SemesterDto;
import shako.schoolmanagement.entity.Semester;
import shako.schoolmanagement.repository.SemesterRepository;
import shako.schoolmanagement.service.inter.SemesterService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    @Override
    public List<SemesterDto> getAllSemesters() {
        log.debug("Fetching all semesters");
        List<Semester> semesters = semesterRepository.getAllSemesters();
        log.debug("Found {} semesters", semesters.size());
        return semesters.stream()
                .map(s -> new SemesterDto(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }
}
