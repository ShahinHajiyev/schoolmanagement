package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.dto.UserDto;


public interface UserService {


    void register(StudentUserDto studentUserDto);

    Boolean isUserExistsByEmail(String email);


}
