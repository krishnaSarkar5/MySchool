package com.MySchool.dto.authenticationDto;

import org.springframework.lang.NonNull;

import com.MySchool.entities.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToken {
	
	@NonNull
	private long id;
	
	@NonNull
	private String username;
	
	@NonNull
	private int status;
	
	@NonNull
	private String channel;
	
	@NonNull
	private User user;

}
