package shako.schoolmanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.repository.EnrollmentRepository;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<Enrollment> getEnrollments() {
        return enrollmentRepository.findAll();
    }
}
