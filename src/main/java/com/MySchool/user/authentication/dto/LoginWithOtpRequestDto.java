package com.MySchool.user.authentication.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginWithOtpRequestDto {

	@NotBlank(message = "username can not be blank")
	private String username;
	
	private Long countryCode;
	
	@NotBlank(message = "otp can not be blank")
	private String otp;
	
	@NotBlank(message = "channel can not be blank")
	private String channel;
}
