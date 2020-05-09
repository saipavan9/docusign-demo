package demo;

import org.springframework.security.crypto.password.PasswordEncoder;

import demo.model.User;
import lombok.Data;

@Data
public class UpdateForm {
	
	private String firstName;	
	private String lastName;
	private String email;
	private String username;
	private String password;
	private String phoneno;
	
	  public User toUser() {
		    return new User(
		    		firstName,lastName,email,username,password, phoneno);
		  }

}
