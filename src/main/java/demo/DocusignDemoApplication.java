package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
public class DocusignDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocusignDemoApplication.class, args);
	}

}
