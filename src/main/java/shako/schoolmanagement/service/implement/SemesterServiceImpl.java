package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.entity.Semester;
import shako.schoolmanagement.repository.SemesterRepository;
import shako.schoolmanagement.service.inter.SemesterService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;
    @Override
    public List<Semester> getAllSemesters() {
        List<Semester> b = semesterRepository.getAllSemesters();
        int a = 1;
        System.out.println("asd");
        return semesterRepository.getAllSemesters();
    }
}
