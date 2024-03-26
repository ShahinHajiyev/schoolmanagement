package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.ActivationCodeDto;
import shako.schoolmanagement.dto.StudentUserDto;
import shako.schoolmanagement.dto.UserDto;
import shako.schoolmanagement.entity.User;


public interface UserService {


    void register(StudentUserDto studentUserDto);

    Boolean isUserExistsByEmail(String email);

    void activateUser(ActivationCodeDto activationCode);

    void saveActivationCode(User user, String activationCode);



}
