package com.MySchool.user.registration.service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.user.registration.dto.RegistrationRequestDto;

public interface RegistrationService {

	public ResponseDto createUser(RegistrationRequestDto registrationRequestDto);
}
