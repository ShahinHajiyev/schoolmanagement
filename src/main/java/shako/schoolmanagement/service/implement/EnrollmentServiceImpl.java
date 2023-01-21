package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.dtomapper.CourseMapper;
import shako.schoolmanagement.dtomapper.EnrollmentMapper;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.repository.EnrollmentRepository;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private  EnrollmentMapper enrollmentMapper;
    @Autowired
    private  StudentMapper studentMapper;
    @Autowired
    private  CourseMapper courseMapper;
    @Autowired
    private  EnrollmentRepository enrollmentRepository;




    @Override
    public List<EnrollmentDto> getAllEnrollments() {

        List<Enrollment> enrollments = enrollmentRepository.getAllEnrollments();

        List<EnrollmentDto> enrollmentDtos = enrollments.stream()
                .map(enrollment -> enrollmentMapper.enrollmentToEnrollmentDto(enrollment)).collect(Collectors.toList());

        for (int i = 0; i < enrollments.size(); i++) {
            enrollmentDtos.get(i).setStudent(studentMapper.studentToStudentDtoForTraining(enrollments.get(i).getStudent()));
            enrollmentDtos.get(i).setCourse(courseMapper.courseToCourseDto(enrollments.get(i).getCourse()));
        }

        return enrollmentDtos;
    }

    @Override
    public void addEnrollment(EnrollmentDto enrollmentDto) {
        System.out.println(enrollmentDto);
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(studentMapper.studentDtoToStudentMapper(enrollmentDto.getStudent()));
        enrollment.setCourse(courseMapper.courseDtoToCourse(enrollmentDto.getCourse()));
        enrollment.setGrade(enrollmentDto.getGrade());
        enrollment.setDateOfRegister(LocalDateTime.now());
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Integer enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        enrollmentRepository.delete(enrollment);
    }
}
