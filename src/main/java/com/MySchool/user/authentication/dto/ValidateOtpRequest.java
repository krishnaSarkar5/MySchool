package com.MySchool.user.authentication.dto;

import lombok.Data;

@Data
public class ValidateOtpRequest {

	private String otp;
	
	private String otpSentTo;
	
	private Integer serviceType;
	
	private Long countryCodeId;
}
