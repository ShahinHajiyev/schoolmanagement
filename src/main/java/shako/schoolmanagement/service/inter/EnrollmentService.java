package shako.schoolmanagement.service.inter;


import shako.schoolmanagement.dto.AddEnrollmentDto;
import shako.schoolmanagement.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService {

    List<EnrollmentDto> getAllEnrollments();

    void addEnrollment(AddEnrollmentDto enrollmentDto);

    void deleteEnrollment(Integer enrollmentId);

    List<EnrollmentDto> getMyEnrollments(String neptunCode);

    boolean isEnrolled(String neptunCode, int courseId);

    void unregisterFromCourse(String neptunCode, int courseId);

    List<EnrollmentDto> getEnrollmentsByStudentNeptunCode(String neptunCode);

    List<EnrollmentDto> getEnrollmentsByCourseId(int courseId);

    void updateGrade(int enrollmentId, int grade);
}
