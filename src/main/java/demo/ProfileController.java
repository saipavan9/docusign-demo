package demo;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import demo.data.UserRepository;
import demo.model.User;

@Controller
public class ProfileController {

	@Autowired
	private UserRepository userRepo;
	
	public ProfileController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@GetMapping("/profile")
	public String getProfile(Model model,Principal principal) {
		
		model.addAttribute("user",userRepo.findByUsername(principal.getName()));
		
		return "profile";
	}
	
	@PostMapping("/update")
	public String updateProfile(@Valid UpdateForm form,Model model,Principal principal,BindingResult result) {
		User temp = userRepo.findByUsername(principal.getName());
		form.setUsername(temp.getUsername());
		form.setPassword(temp.getPassword());
//		model.addAttribute("error");
		User user = form.toUser();
		user.setId(temp.getId());
		userRepo.save(user);
		model.addAttribute("user",user);
		model.addAttribute("status","success");

		return "profile";
	}
	
}
