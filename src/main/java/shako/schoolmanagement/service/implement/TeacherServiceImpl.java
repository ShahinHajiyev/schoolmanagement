package shako.schoolmanagement.service.implement;

import org.springframework.stereotype.Service;
import shako.schoolmanagement.dto.TeacherDto;
import shako.schoolmanagement.entity.Teacher;
import shako.schoolmanagement.service.inter.TeacherService;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Override
    public void addTeacher(TeacherDto teacherDto) {

    }

    @Override
    public List<Teacher> getAllTeachers() {
        return null;
    }
}
