package shako.schoolmanagement.validator;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class LoginActivator {

    private static final String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String activationTokenGenerator(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(validCharacters.length());
            char randomChar = validCharacters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
