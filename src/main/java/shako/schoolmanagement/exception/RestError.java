package shako.schoolmanagement.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
public class RestError {

    private String status;
    private String message;

    @Autowired
    public RestError(String status, String message) {
        this.status = status;
        this.message = message;
    }


}
