package com.MySchool.security;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.MySchool.dto.authenticationDto.UserToken;
import com.MySchool.entities.user.User;
import com.MySchool.entities.user.UserLoginToken;
import com.MySchool.repositories.UserLoginTokenRepository;
import com.MySchool.repositories.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUserDetailService jwtUserDetailService;
	
	@Autowired
	private JwtUtils jwtUtils; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserLoginTokenRepository userLoginTokenRepository;
	
	@Autowired
	private Environment environment;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String requestTokenHeader = request.getHeader("Authorization");
//		System.out.println(request.getRequestURI());
		String profileId = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				profileId = jwtUtils.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
	
		// Once we get the token validate it.
		if (profileId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(profileId);
			User user = null;
			
			//validates if the username is phone number or email address
			user = userRepository.findByProfileId(profileId);
			
			UserLoginToken userLoginToken = userLoginTokenRepository.findByUser_idAndTokenAndStatus(user.getId(), requestTokenHeader, 1);
			boolean isTokenActive = !Objects.isNull(userLoginToken);
			

			
			
			boolean isTokenValidForActiveUsers = jwtUtils.validateTokenWExpirationValidation(jwtToken, userDetails)
					&& user.getStatus() == Integer.parseInt(environment.getProperty("active"));
			

			
			if (isTokenActive && isTokenValidForActiveUsers)
			{
				UserToken principal=new UserToken();
				principal.setId(user.getId());
				principal.setStatus(user.getStatus());
				principal.setUsername(Objects.isNull(user.getEmail())?user.getMasterCountryCode().getCountryCode()+user.getPhoneNo():user.getEmail());
				principal.setChannel(userLoginToken.getChannel());
				principal.setUser(user);
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						principal, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		System.out.println("out of filter");
		filterChain.doFilter(request, response);
	}



}
