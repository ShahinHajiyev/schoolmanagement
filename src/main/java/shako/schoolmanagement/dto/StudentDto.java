package shako.schoolmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import shako.schoolmanagement.entity.Student;

import java.time.LocalDateTime;


@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private int userId;
    private String firstName;
    private String lastName;
    private String neptunCode;
    private String email;
    private String country;
    private int rollNumber;
    private LocalDateTime graduationYear;

}
