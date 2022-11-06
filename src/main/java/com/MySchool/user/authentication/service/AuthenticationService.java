package com.MySchool.user.authentication.service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.user.authentication.dto.SentOtpRequestDto;

public interface AuthenticationService {

	public ResponseDto sentOtp(SentOtpRequestDto sentOtpRequestDto);
}
