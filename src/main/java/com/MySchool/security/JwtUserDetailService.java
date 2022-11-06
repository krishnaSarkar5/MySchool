package com.MySchool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.MySchool.entities.user.User;
import com.MySchool.repositories.UserRepository;



@Service
public class JwtUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepositoy;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// loading user from database
		
		User user = userRepositoy.findByEmail(username).get();
		
		
		
		
		return new JwtUserDetails(user);
	}

}
