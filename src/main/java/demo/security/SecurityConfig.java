package demo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	  private UserDetailsService userDetailsService;
	  
	//end::customUserDetailsService[]

	  //tag::configureHttpSecurity[]
	  //tag::authorizeRequests[]
	  //tag::customLoginPage[]
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http
	      .authorizeRequests().anyRequest().permitAll()
	      .and()
	        .formLogin()
	          .loginPage("/")
	          .defaultSuccessUrl("/home")
	          .permitAll(true)
	      .and()
	        .logout()
	          .logoutSuccessUrl("/")
	          .permitAll()
	          
	      .and()
	        .csrf().disable()
	        
	      
	      ;
	    
	    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
	  }
	  
	  @SuppressWarnings("deprecation")
	@Bean
	  public PasswordEncoder encoder() {
	    return new StandardPasswordEncoder("53cr3t");
	  }
	  
	  
	  @Override
	  protected void configure(AuthenticationManagerBuilder auth)
	      throws Exception {

	    auth
	      .userDetailsService(userDetailsService)
	      .passwordEncoder(encoder());
	    
	  }
}
