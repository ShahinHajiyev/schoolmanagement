package shako.schoolmanagement.dtomapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import shako.schoolmanagement.dto.UserDto;
import shako.schoolmanagement.entity.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@NoArgsConstructor
@Configuration
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserDto userEntityToDto(User user){

        return modelMapper.map(user, UserDto.class);
    }

    public User dtoToUserEntity(UserDto userDto){

        return modelMapper.map(userDto, User.class);
    }
}
