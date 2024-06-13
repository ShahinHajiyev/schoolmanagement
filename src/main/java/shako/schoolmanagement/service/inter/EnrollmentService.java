package shako.schoolmanagement.service.inter;


import shako.schoolmanagement.dto.AddEnrollmentDto;
import shako.schoolmanagement.dto.EnrollmentDto;
import java.util.List;

public interface EnrollmentService {


    List<EnrollmentDto> getAllEnrollments();

    //void addEnrollment(EnrollmentDto enrollmentDto);

    void addEnrollment(AddEnrollmentDto enrollmentDto);

    void deleteEnrollment(Integer enrollmentId);
}
