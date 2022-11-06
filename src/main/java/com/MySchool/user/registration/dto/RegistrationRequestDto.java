package com.MySchool.user.registration.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
public class RegistrationRequestDto {

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private Long countryCodeId;
	
	private String phoneNo;
	
	private String password;
}
