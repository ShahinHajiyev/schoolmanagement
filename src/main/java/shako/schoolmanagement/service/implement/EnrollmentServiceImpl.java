package shako.schoolmanagement.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.AddEnrollmentDto;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.dtomapper.CourseMapper;
import shako.schoolmanagement.dtomapper.EnrollmentMapper;
import shako.schoolmanagement.dtomapper.StudentMapper;
import shako.schoolmanagement.entity.Course;
import shako.schoolmanagement.entity.Enrollment;
import shako.schoolmanagement.entity.Student;
import shako.schoolmanagement.entity.User;
import shako.schoolmanagement.exception.EnrollmentOutOfLimitException;
import shako.schoolmanagement.exception.EnrollmentOutOfTimeException;
import shako.schoolmanagement.repository.CourseRepository;
import shako.schoolmanagement.repository.EnrollmentRepository;
import shako.schoolmanagement.repository.StudentRepository;
import shako.schoolmanagement.service.inter.EnrollmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;




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
    public void addEnrollment(AddEnrollmentDto enrollmentDto) {
        Optional<Student> loggedInStudent = studentRepository.findByNeptunCode(enrollmentDto.getNeptunCode());
        System.out.println(enrollmentDto);
        Enrollment enrollmentOfStudent = new Enrollment();
        enrollmentOfStudent.setStudent(loggedInStudent.get());
        Course course = courseRepository.getCourseByCourseId(enrollmentDto.getCourseId());
        enrollmentOfStudent.setCourse(course);
        enrollmentOfStudent.setDateOfRegister(LocalDateTime.now());


        List<Enrollment> enrollmentList = enrollmentRepository.enrollmentsOfStudent(enrollmentDto.getCourseId(), loggedInStudent.get().getUserId());

        if (enrollmentList.isEmpty()) {
            enrollmentRepository.save(enrollmentOfStudent);
            //ToDo next request to remove the course from the available courses
        }

        else {
            for (Enrollment a : enrollmentList) {
                if (enrollmentList.size() < 3 && a.getDateOfRegister().plusMonths(3).isBefore(LocalDate.now().atStartOfDay())) {
                    enrollmentRepository.save(enrollmentOfStudent);
                    System.out.println("SOMETHING");
                    System.err.println(a.getDateOfRegister());
                    break;
                } else if (enrollmentList.size() >= 3) {
                    throw  new EnrollmentOutOfLimitException("Student has reached the limit of enrollment");
                } else if (!a.getDateOfRegister().plusMonths(3).isBefore(LocalDate.now().atStartOfDay())) {
                    throw  new EnrollmentOutOfTimeException("Student has enrolled the course during the last 3 months");
                }
            }
        }

    }

    @Override
    public void deleteEnrollment(Integer enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId);
        enrollmentRepository.delete(enrollment);
    }
}
