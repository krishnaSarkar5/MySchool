package com.MySchool.user.authentication.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class LoginWithPasswordRequestDto {

//	@NotNull(message = "username can not be null")
	@NotBlank(message = "username can not be blank")
	private String username;
	
//	@NotNull
	@NotBlank(message = "password can not be blank")
	private String password;
	
	@NotBlank(message = "channel can not be blank")
	private String channel;
}
