package com.MySchool.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.MySchool.entities.user.User;
import com.MySchool.repositories.UserRepository;



@Service
public class JwtUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Environment environment;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// loading user from database
		
		User user = userRepository.findByProfileId(username);
		
		boolean isActive = false;
		
		if(user.getStatus()==Integer.parseInt(environment.getProperty("active")))
		{
			isActive = true;
		}
		
				
		UserDetails userDetails = new JwtUserDetails(username, user.getPassword(), Arrays.asList(user.getRole().getTitle()), isActive);
		
		return userDetails;
	}

}
