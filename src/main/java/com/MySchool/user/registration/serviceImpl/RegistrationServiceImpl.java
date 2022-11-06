package com.MySchool.user.registration.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.repositories.UserRepository;
import com.MySchool.user.registration.dto.RegistrationRequestDto;
import com.MySchool.user.registration.service.RegistrationService;



@Service
public class RegistrationServiceImpl implements RegistrationService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseDto createUser(RegistrationRequestDto registrationRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
