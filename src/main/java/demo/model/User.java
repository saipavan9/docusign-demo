package demo.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE,force = true)
@RequiredArgsConstructor
public class User implements UserDetails{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private  Long id;
	
	@NotBlank
	private final String firstName;
	
	@NotBlank
	private final String lastName;
	
	@Email
	@NotBlank
	private final String email;
	
	@Column(unique = true)
	@NotBlank
	private final String username;
	
	@Size(min = 8, message = "Please enter between {min} characters.")
	@NotBlank
	private final String password;
	
	@Size(min=10,max=10, message = "Please enter {min} characters.")
	private final String phoneno;
	
//	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//	private List<Role> roles;
	
	@Override
	  public Collection<? extends GrantedAuthority> getAuthorities() {
	    return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	  }

	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isEnabled() {
	    return true;
	  }

}