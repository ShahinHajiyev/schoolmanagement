package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.AdminStudentDto;
import shako.schoolmanagement.dto.AdminUserListDto;

import java.util.List;

public interface AdminService {

    void addUserByAdmin(AdminStudentDto adminStudentDto);

    List<AdminUserListDto> getAllUsers();
}
