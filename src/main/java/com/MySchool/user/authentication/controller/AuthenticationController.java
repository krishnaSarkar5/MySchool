package com.MySchool.user.authentication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.user.authentication.dto.LoginWithOtpRequestDto;
import com.MySchool.user.authentication.dto.LoginWithPasswordRequestDto;
import com.MySchool.user.authentication.dto.SentOtpRequestDto;
import com.MySchool.user.authentication.dto.ValidateOtpRequest;
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
	
	@PostMapping("/validateotp")
	public ResponseEntity<ResponseDto> validateOtp(@Valid @RequestBody ValidateOtpRequest validateOtpRequest) {
		ResponseDto response = authenticationService.validateOtp(validateOtpRequest);
		return new ResponseEntity<ResponseDto>(response,HttpStatus.OK);
	}
	
	@PostMapping("/loginwithpassword")
	public ResponseDto loginwithpassword(@Valid @RequestBody LoginWithPasswordRequestDto loginWithPasswordRequestDto) {
		ResponseDto response = authenticationService.loginWithPassword(loginWithPasswordRequestDto);
		return response;
	}
	
	@PostMapping("/loginwithotp")
	public ResponseDto loginwithotp(@Valid @RequestBody LoginWithOtpRequestDto loginWithOtpRequestDto) {
		ResponseDto response = authenticationService.loginWithOtp(loginWithOtpRequestDto);
		return response;
	}
	
	@GetMapping("/logout")
	public ResponseDto logout(@RequestHeader("Authorization") String Authorization) {
		ResponseDto response = authenticationService.logout(Authorization);
		return response;
	}
}
