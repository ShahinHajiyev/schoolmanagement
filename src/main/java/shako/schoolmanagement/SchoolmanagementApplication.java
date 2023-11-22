package shako.schoolmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import shako.schoolmanagement.service.implement.MailServiceImpl;
import shako.schoolmanagement.service.inter.MailService;

@SpringBootApplication
public class SchoolmanagementApplication {

	@Autowired
	private MailService mailService;

	public static void main(String[] args) {
		SpringApplication.run(SchoolmanagementApplication.class, args);
	}

	//@EventListener(ApplicationReadyEvent.class)
	//public void sendMail(){
     //      mailService.sendMail("magicboy.shako@gmail.com",
		//		                        "This is the email",
		//		                                "This is the body");
	//}

}
