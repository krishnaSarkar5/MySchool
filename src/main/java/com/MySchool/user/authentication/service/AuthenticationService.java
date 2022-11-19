package com.MySchool.user.authentication.service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.user.authentication.dto.LoginWithOtpRequestDto;
import com.MySchool.user.authentication.dto.LoginWithPasswordRequestDto;
import com.MySchool.user.authentication.dto.SentOtpRequestDto;
import com.MySchool.user.authentication.dto.ValidateOtpRequest;

public interface AuthenticationService {

	public ResponseDto sentOtp(SentOtpRequestDto sentOtpRequestDto);
	
	public ResponseDto validateOtp(ValidateOtpRequest validateOtpRequest);
	
	public ResponseDto loginWithPassword(LoginWithPasswordRequestDto loginWithPasswordRequestDto);
	
	public ResponseDto loginWithOtp(LoginWithOtpRequestDto loginWithOtpRequestDto);
}
