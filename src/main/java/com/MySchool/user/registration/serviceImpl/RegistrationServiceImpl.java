package com.MySchool.user.registration.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.entities.authentication.OtpLog;
import com.MySchool.entities.master.Role;
import com.MySchool.entities.teacher.TeacherProfile;
import com.MySchool.entities.user.User;
import com.MySchool.exception.ServiceException;
import com.MySchool.master.entities.MasterCountryCode;
import com.MySchool.master.repositories.MasterCountryCodeRepository;
import com.MySchool.repositories.RoleRepository;
import com.MySchool.repositories.UserRepository;
import com.MySchool.repositories.authentication.OtpLogRepository;
import com.MySchool.repositories.teacher.TeacherProfileRepository;
import com.MySchool.user.registration.dto.RegistrationRequestDto;
import com.MySchool.user.registration.service.RegistrationService;
import com.MySchool.utility.CommonUtils;
import com.MySchool.utility.ErrorMessages;



@Service
public class RegistrationServiceImpl implements RegistrationService {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private OtpLogRepository otpLogRepository;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private ErrorMessages errorMessages;
	
	@Autowired
	private TeacherProfileRepository teacherProfileRepository;
	
	@Autowired
	private MasterCountryCodeRepository masterCountryCodeRepository;
	
	@Autowired
	private CommonUtils commonUtils;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public ResponseDto createUser(RegistrationRequestDto registrationRequestDto) {
		
		ResponseDto responseDto = null;
		
		this.dataValidationRegistrationRequestDto(registrationRequestDto);
		
		if(registrationRequestDto.getRoleId()==Long.parseLong(environment.getProperty("teacher"))) {
			 responseDto = this.registerTeacher(registrationRequestDto);
		}
		
		
		return responseDto;
	}
	
	
	private ResponseDto registerTeacher(RegistrationRequestDto registrationRequestDto) {
		
		ResponseDto responseDto = new ResponseDto();
		
		
		
		
		OtpLog emailValidationOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(registrationRequestDto.getEmail(), Integer.parseInt(environment.getProperty("signup")));
		
		OtpLog phoneValidationOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(registrationRequestDto.getPhoneNo(), Integer.parseInt(environment.getProperty("signup")));
	
		if(!emailValidationOtpLog.isValidated() || !phoneValidationOtpLog.isValidated()) {
			throw new ServiceException(errorMessages.getErrorMessages("EMAIL_PHONE_NOT_VALIDATED"));
		}
		
		
		MasterCountryCode existedCountryCode = masterCountryCodeRepository.findByIdAndStatus(registrationRequestDto.getCountryCodeId(), Integer.parseInt(environment.getProperty("active")));

		if(Objects.isNull(existedCountryCode)) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_COUNTRY_CODE"));
		}
		
		Optional<Role> existedRole = roleRepository.findByIdAndStatus(registrationRequestDto.getRoleId(), Integer.parseInt(environment.getProperty("active")));
		
		if(existedRole.isEmpty()) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_ROLE"));
		}
		
		
		User newTeacher = new User(registrationRequestDto);
		newTeacher.setMasterCountryCode(existedCountryCode);
		newTeacher.setRole(existedRole.get());
		newTeacher.setProfileId(commonUtils.generateProfileId(newTeacher));
		newTeacher.setStatus(Integer.parseInt(environment.getProperty("active")));
		
		
		User savedTeacher = userRepository.save(newTeacher);
		
		TeacherProfile teacherProfile = new TeacherProfile();
		teacherProfile.setCreatedAt(LocalDateTime.now());
		teacherProfile.setUser(savedTeacher);
		
		teacherProfileRepository.save(teacherProfile);
		
		responseDto.setStatus(true);
		responseDto.setMessage("SUCCESSFULL");
		responseDto.setData("Profile created successfully");
		
		
		return responseDto;
	}
	 
	
	
	
	private void dataValidationRegistrationRequestDto(RegistrationRequestDto registrationRequestDto) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		// first name validation
		if(Objects.isNull(registrationRequestDto.getFirstName()) || registrationRequestDto.getFirstName().equals("")) {
			errorMap.put("First Name", "First Name can not be null");
		}
		
		// last name validation
		if(Objects.isNull(registrationRequestDto.getLastName()) || registrationRequestDto.getLastName().equals("")) {
			errorMap.put("Last Name", "Last Name can not be null");
		}
		
		// role id validation
		if(Objects.isNull(registrationRequestDto.getRoleId()) || registrationRequestDto.getRoleId()==0L) {
			errorMap.put("role id", "Invalid role id");
		}
		// if role is teacher then both email and phone is mandatory
		else if (registrationRequestDto.getRoleId()==Long.parseLong(environment.getProperty("teacher"))) {
			if(Objects.isNull(registrationRequestDto.getEmail()) || registrationRequestDto.getEmail().equals("")) {
				errorMap.put("Email", "Email can not be null");
			}else if (!commonUtils.isEmailValid(registrationRequestDto.getEmail())) {
				errorMap.put("Email", "Invalid email id");
			}
			
			if(Objects.isNull(registrationRequestDto.getPhoneNo()) || registrationRequestDto.getPhoneNo().equals("")) {
				errorMap.put("Phone no", "Phone no can not be null");
			}else if (!commonUtils.isPhoneNumberValid(registrationRequestDto.getPhoneNo(),registrationRequestDto.getCountryCodeId())) {
				errorMap.put("Phone No", "Invalid Phone no");
			}
		}
		// if role is student any one among email and phone is mandatory
		else if (registrationRequestDto.getRoleId()==Long.parseLong(environment.getProperty("student"))) {
			
			
			if( (Objects.isNull(registrationRequestDto.getEmail()) && Objects.isNull(registrationRequestDto.getPhoneNo()))
				|| (registrationRequestDto.getEmail().trim().equals("") && registrationRequestDto.getPhoneNo().trim().equals("")) ) {
				
				errorMap.put("Email Or Phone No", "For student one contact medium is mandatory");
			}else {
				if(Objects.isNull(registrationRequestDto.getEmail()) || registrationRequestDto.getEmail().trim().equals("")) {
					if(!commonUtils.isEmailValid(registrationRequestDto.getEmail())){
						errorMap.put("Email", "Invalid email id");
					}
				}else if (Objects.isNull(registrationRequestDto.getPhoneNo()) || registrationRequestDto.getPhoneNo().trim().equals("")) {
					if(!commonUtils.isPhoneNumberValid(registrationRequestDto.getEmail(),registrationRequestDto.getCountryCodeId())){
						errorMap.put("Email", "Invalid email id");
					}
				}
			}
		}
		
		
		
		// Password validation
		if(Objects.isNull(registrationRequestDto.getPassword()) || registrationRequestDto.getPassword().equals("")) {
			errorMap.put("Password", "Password can not be null");
		}else if (!commonUtils.isValidPassword(registrationRequestDto.getPassword())) {
			errorMap.put("Password", "Password lenght should be in 8 to 20 Characters, containing Atleast an upperCase, a lower case,a special character");
		}
		
		
		
		if(errorMap.size()!=0) {
			throw new ServiceException("Invalid input data",errorMap);
		}
		
		// check wether user already exist or not
		Optional<User> existedUser = userRepository.findByEmailOrPhoneNo(registrationRequestDto.getEmail(), registrationRequestDto.getPhoneNo());
		
		if(existedUser.isPresent()) {
			throw new ServiceException(errorMessages.getErrorMessages("ALREADY_REGISTERED"));
		}
	}

}
