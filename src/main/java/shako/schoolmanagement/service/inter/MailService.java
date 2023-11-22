package shako.schoolmanagement.service.inter;

public interface MailService {

    void sendMail(String toMail,
                  String subject,
                  String body);

}
