package shako.schoolmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shako.schoolmanagement.service.inter.MailService;

@SpringBootApplication
public class SchoolmanagementApplication {

	@Autowired
	private MailService mailService;

	public static void main(String[] args) {
		SpringApplication.run(SchoolmanagementApplication.class, args);
	}


}
