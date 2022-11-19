package com.MySchool.user.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.user.registration.dto.RegistrationRequestDto;
import com.MySchool.user.registration.service.RegistrationService;

@RestController
@RequestMapping("/user/registration")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;
	
	@PostMapping("/newuser")
	public ResponseEntity<ResponseDto> registerUser(@RequestBody RegistrationRequestDto registrationRequestDto){
		
		ResponseDto responseDto = registrationService.createUser(registrationRequestDto);
		
		return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
		
	}
	
}
