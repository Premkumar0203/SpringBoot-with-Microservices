package com.digitalbooks.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digitalbooks.entities.Users;
import com.digitalbooks.repositories.UsersRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
    UsersRepository UserResp;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		 Optional<Users> user = 	UserResp.fetchUserName(username);
			if(user.isPresent()) {
				Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		        authorities.add(new SimpleGrantedAuthority("ROLE_" + "AUTHOR"));
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				return new User(username, encoder.encode(user.get().getPassword()), authorities);
			}  else {
				throw new UsernameNotFoundException("User not found with username: " + username);
			}	
	}
	
	public UserDetails loadUserByUsernameAndPassword(String username, String password) throws UsernameNotFoundException
	{

      Optional<Users> user = 	UserResp.getUserDetails(username,password);
		if(user.isPresent()) {
			Set<SimpleGrantedAuthority> authorities = new HashSet<>();
	        authorities.add(new SimpleGrantedAuthority("ROLE_" + "AUTHOR"));
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			return new User(username, encoder.encode(user.get().getPassword()), authorities);
		}  else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
