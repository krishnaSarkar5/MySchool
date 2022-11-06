package com.MySchool.utility;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.MySchool.dto.authenticationDto.UserToken;

@Component
public class AuthenticationUtil {

	public UserToken currentLoggedInUser() {
		return (UserToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
