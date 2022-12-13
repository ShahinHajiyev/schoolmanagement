package shako.schoolmanagement.service.inter;

import org.springframework.data.jpa.repository.Query;
import shako.schoolmanagement.entity.Enrollment;

import java.util.List;

public interface EnrollmentService {


    List<Enrollment> getEnrollments();
}
