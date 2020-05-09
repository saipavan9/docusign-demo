package demo.security;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.nimbusds.oauth2.sdk.id.ClientID;

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
	  
	  
//	  public ClientRegistrationRepository clientRepo() {
//		  List<ClientRegistration> registrations = new ArrayList<>();
//			registrations.add(docusignRegistration());
//			return new InMemoryClientRegistrationRepository(registrations);
//	  }
//	  
//	  
//	  private ClientRegistration docusignRegistration() {
//		  return ClientRegistration.withRegistrationId("docusign")
//				  .clientId("5515224b-6c68-4fdb-b212-f5491f38c4a1")
//				  .clientSecret("0af8229e-60b9-4249-9782-2dea7710eed6")
//				  .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				  .redirectUriTemplate("http://localhost:8080/home")
//				  .authorizationUri("https://account-d.docusign.com/oauth/auth")
//				  .tokenUri("https://account-d.docusign.com/oauth/token")
//				  .userInfoUri("https://account-d.docusign.com/oauth/userinfo")
//				  .clientName("docusign").build();
//		
//	  }
}
