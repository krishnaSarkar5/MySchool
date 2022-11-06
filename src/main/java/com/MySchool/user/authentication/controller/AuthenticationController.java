package com.MySchool.user.authentication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.user.authentication.dto.SentOtpRequestDto;
import com.MySchool.user.authentication.service.AuthenticationService;

@RestController
@RequestMapping("/user/authenticate")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;
	
	
	@PostMapping("/sentotp")
	public ResponseEntity<ResponseDto> sentOtp(@Valid @RequestBody SentOtpRequestDto sentOtpRequestDto) {
		ResponseDto response = authenticationService.sentOtp(sentOtpRequestDto);
		return new ResponseEntity<ResponseDto>(response,HttpStatus.OK);
	}
}
