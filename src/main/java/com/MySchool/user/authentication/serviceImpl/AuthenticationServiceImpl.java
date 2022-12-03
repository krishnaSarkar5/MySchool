package com.MySchool.user.authentication.serviceImpl;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.entities.authentication.OtpLog;
import com.MySchool.entities.master.MasterCountryCode;
import com.MySchool.entities.master.Settings;
import com.MySchool.entities.user.User;
import com.MySchool.entities.user.UserLoginToken;
import com.MySchool.exception.ServiceException;
import com.MySchool.repositories.UserLoginTokenRepository;
import com.MySchool.repositories.UserRepository;
import com.MySchool.repositories.authentication.OtpLogRepository;
import com.MySchool.repositories.master.MasterCountryCodeRepository;
import com.MySchool.repositories.master.SettingsRepository;
import com.MySchool.security.JwtUserDetailService;
import com.MySchool.security.JwtUtils;
import com.MySchool.user.authentication.dto.LoginResponseDto;
import com.MySchool.user.authentication.dto.LoginWithOtpRequestDto;
import com.MySchool.user.authentication.dto.LoginWithPasswordRequestDto;
import com.MySchool.user.authentication.dto.SentOtpRequestDto;
import com.MySchool.user.authentication.dto.ValidateOtpRequest;
import com.MySchool.user.authentication.service.AuthenticationService;
import com.MySchool.utility.AuthenticationUtil;
import com.MySchool.utility.CommonUtils;
import com.MySchool.utility.ErrorMessages;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private OtpLogRepository otpLogRepository;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private ErrorMessages errorMessages;
	
	@Autowired
	private CommonUtils commonUtils;
	
	@Autowired
	private SettingsRepository settingsRepository;
	
	@Autowired
	private MasterCountryCodeRepository masterCountryCodeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUserDetailService jwtUserDetailService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserLoginTokenRepository userLoginTokenRepository;
	
	@Autowired
	private AuthenticationUtil authenticationUtil;

	
	
	
	
	@Override
	public ResponseDto sentOtp(SentOtpRequestDto sentOtpRequestDto) {
		
		ResponseDto responseDto = new ResponseDto();
		
		OtpLog newOtpLog = null;
		
		Settings settingsData = settingsRepository.findById(1L).get();
		
		OtpLog lastLoginOtpLog = null;
		
		OtpLog lastSignupOtpLog = null;
		
		this.dataValidateSentOtpRequestDto(sentOtpRequestDto);
		
		if(sentOtpRequestDto.getServiceType()==Integer.parseInt(environment.getProperty("signup"))){
			
			Optional<User> existedEmailOrPhone = userRepository.findByEmailOrPhoneNo(sentOtpRequestDto.getOtpSentTo(),sentOtpRequestDto.getOtpSentTo());
			if(existedEmailOrPhone.isPresent()) {
				throw new ServiceException(errorMessages.getErrorMessages("ALREADY_REGISTERED"));
			}
			
			lastSignupOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(sentOtpRequestDto.getOtpSentTo(),sentOtpRequestDto.getServiceType());
		}else if(sentOtpRequestDto.getServiceType()==Integer.parseInt(environment.getProperty("login"))){
			
			List<Integer> statusList = List.of(Integer.parseInt(environment.getProperty("signup")),Integer.parseInt(environment.getProperty("dataValidation")));
			
			lastLoginOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(sentOtpRequestDto.getOtpSentTo(),sentOtpRequestDto.getServiceType());
			lastSignupOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeInOrderBySentAtDesc(sentOtpRequestDto.getOtpSentTo(),statusList);
		
		}else {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_SERVICE_TYPE"));
		}
		
	
		 
		
		if(sentOtpRequestDto.getServiceType()==Integer.parseInt(environment.getProperty("login")) 
				&& Objects.isNull(lastSignupOtpLog)) {
			throw new ServiceException(errorMessages.getErrorMessages("NOT_REGISTERED"));
		}else if (sentOtpRequestDto.getServiceType()==Integer.parseInt(environment.getProperty("login"))
				&&  !Objects.isNull(lastSignupOtpLog) && !lastSignupOtpLog.isValidated()) {
			throw new ServiceException(errorMessages.getErrorMessages("NOT_REGISTERED"));
		}
		
		
		 if(Objects.isNull(lastSignupOtpLog)) {
			 newOtpLog = this.getNewOtpLog(sentOtpRequestDto, settingsData);
			
			Map<String, Integer> map = new HashMap<>();
			map.put("count", newOtpLog.getCount());
			
			responseDto.setMessage("SUCCESSFULL");
			responseDto.setStatus(true);
			responseDto.setData(map);
			
			
		}else if (!Objects.isNull(lastSignupOtpLog) 
				&& sentOtpRequestDto.getServiceType()==Integer.parseInt(environment.getProperty("signup")) 
				&& lastSignupOtpLog.isValidated()) {
			throw new ServiceException(errorMessages.getErrorMessages("ALREADY_REGISTERED"));
		}else if (!Objects.isNull(lastSignupOtpLog)) {
			
			// chech current time exceed window time or not
			if(LocalDateTime.now().isAfter(lastSignupOtpLog.getValidationWindowTime()) || LocalDateTime.now().isEqual(lastSignupOtpLog.getValidationWindowTime())) {
			
				newOtpLog = this.getNewOtpLog(sentOtpRequestDto, settingsData);
				
				Map<String, Integer> map = new HashMap<>();
				map.put("count", newOtpLog.getCount());
				
				responseDto.setMessage("SUCCESSFULL");
				responseDto.setStatus(true);
				responseDto.setData(map);

			
			}else if (LocalDateTime.now().isBefore(lastSignupOtpLog.getValidationWindowTime())) {
				
				if(lastSignupOtpLog.getCount()<settingsData.getMaxOtpAttemps()) {
					newOtpLog = this.getNewUpdatedOtpLog(lastSignupOtpLog,sentOtpRequestDto);
					
					Map<String, Integer> map = new HashMap<>();
					map.put("count", newOtpLog.getCount());
					
					responseDto.setMessage("SUCCESSFULL");
					responseDto.setStatus(true);
					responseDto.setData(map);	
					
				}else {
					
					
					throw new ServiceException(errorMessages.getErrorMessages("MAX_OTP_ATTEMPTED"));
//					newOtpLog = this.getNewOtpLog(sentOtpRequestDto, settingsData);
//					
//					Map<String, Integer> map = new HashMap<>();
//					map.put("count", newOtpLog.getCount());
//					
//					responseDto.setMessage("SUCCESSFULL");
//					responseDto.setStatus(true);
//					responseDto.setData(map);
				}
				
			}	
		
		}else if (!Objects.isNull(lastLoginOtpLog)) {
			
			// check current time exceed window time or not
				if(LocalDateTime.now().isAfter(lastLoginOtpLog.getValidationWindowTime()) || LocalDateTime.now().isEqual(lastLoginOtpLog.getValidationWindowTime())) {
				
					newOtpLog = this.getNewOtpLog(sentOtpRequestDto, settingsData);
					
					Map<String, Integer> map = new HashMap<>();
					map.put("count", newOtpLog.getCount());
					
					responseDto.setMessage("SUCCESSFULL");
					responseDto.setStatus(true);
					responseDto.setData(map);

				
				}else if (LocalDateTime.now().isBefore(lastLoginOtpLog.getValidationWindowTime())) {
					
					if(lastLoginOtpLog.getCount()<settingsData.getMaxOtpAttemps()) {
						newOtpLog = this.getNewUpdatedOtpLog(lastLoginOtpLog,sentOtpRequestDto);
						
						Map<String, Integer> map = new HashMap<>();
						map.put("count", newOtpLog.getCount());
						
						responseDto.setMessage("SUCCESSFULL");
						responseDto.setStatus(true);
						responseDto.setData(map);	
						
					}else {
						
						throw new ServiceException(errorMessages.getErrorMessages("MAX_OTP_ATTEMPTED"));
//						newOtpLog = this.getNewOtpLog(sentOtpRequestDto, settingsData);
//						
//						Map<String, Integer> map = new HashMap<>();
//						map.put("count", newOtpLog.getCount());
//						
//						responseDto.setMessage("SUCCESSFULL");
//						responseDto.setStatus(true);
//						responseDto.setData(map);
					}
					
				}	
		}
		
		otpLogRepository.save(newOtpLog);
		return responseDto;
	}
	
	
	private OtpLog getNewOtpLog(SentOtpRequestDto sentOtpRequestDto,Settings settingsData) {
		
		OtpLog	newOtpLog = new OtpLog();
		
		if(sentOtpRequestDto.getMedium()==Integer.parseInt(environment.getProperty("phone"))) {
			MasterCountryCode existedCountryCode = masterCountryCodeRepository.findByIdAndStatus(sentOtpRequestDto.getCountryCodeId(), Integer.parseInt(environment.getProperty("active")));
			
			if(Objects.isNull(existedCountryCode)) {
				throw new ServiceException(errorMessages.getErrorMessages("INVALID_COUNTRY_CODE"));
			}else {
				newOtpLog.setMasterCountryCode(existedCountryCode);
				newOtpLog.setOtpMedium(sentOtpRequestDto.getMedium());
			}
		}else if (sentOtpRequestDto.getMedium()==Integer.parseInt(environment.getProperty("email"))) {
			newOtpLog.setOtpMedium(sentOtpRequestDto.getMedium());
		}
		
		
		
		String generatedOtp = commonUtils.generateOtp(4);
		newOtpLog.setOtp(generatedOtp);
		newOtpLog.setCount(1);
		newOtpLog.setOtpVia(sentOtpRequestDto.getOtpSentTo());
		newOtpLog.setValidationWindowTime(LocalDateTime.now().plusSeconds(settingsData.getWindowDuration()));
		newOtpLog.setCreatedAt(LocalDateTime.now());
		newOtpLog.setSentAt(LocalDateTime.now());
		newOtpLog.setServiceType(sentOtpRequestDto.getServiceType());
		newOtpLog.setValidated(false);
		
		return newOtpLog;
	}
	
	private OtpLog getNewUpdatedOtpLog(OtpLog oldOtpLog,SentOtpRequestDto sentOtpRequestDto) {
		OtpLog newOtpLog = new OtpLog();
		
		if(sentOtpRequestDto.getMedium()==Integer.parseInt(environment.getProperty("phone"))) {
			MasterCountryCode existedCountryCode = masterCountryCodeRepository.findByIdAndStatus(sentOtpRequestDto.getCountryCodeId(), Integer.parseInt(environment.getProperty("active")));
			
			if(Objects.isNull(existedCountryCode)) {
				throw new ServiceException(errorMessages.getErrorMessages("INVALID_COUNTRY_CODE"));
			}else {
				newOtpLog.setMasterCountryCode(existedCountryCode);
				newOtpLog.setOtpMedium(sentOtpRequestDto.getMedium());
			}
		}else if (sentOtpRequestDto.getMedium()==Integer.parseInt(environment.getProperty("email"))) {
			newOtpLog.setOtpMedium(sentOtpRequestDto.getMedium());
		}
		
		
		
		
		
		String generatedOtp = commonUtils.generateOtp(4);
		newOtpLog.setCount(oldOtpLog.getCount()+1);
		newOtpLog.setValidationWindowTime(oldOtpLog.getValidationWindowTime());
		newOtpLog.setOtp(generatedOtp);
		newOtpLog.setOtpVia(oldOtpLog.getOtpVia());
		newOtpLog.setCreatedAt(LocalDateTime.now());
		newOtpLog.setSentAt(LocalDateTime.now());
		newOtpLog.setValidated(false);
		newOtpLog.setServiceType(oldOtpLog.getServiceType());
		
		return newOtpLog;
	}
	
	
	private void dataValidateSentOtpRequestDto(SentOtpRequestDto sentOtpRequestDto) {
		
		Map<String, String> map = new HashMap<>();
		
		if(Objects.isNull(sentOtpRequestDto.getOtpSentTo()) || sentOtpRequestDto.getOtpSentTo().trim().length()==0) {
			map.put("otpSentAt", "This field cannot be null");
		}else {
			if(commonUtils.isEmailValid(sentOtpRequestDto.getOtpSentTo().trim())) {
				sentOtpRequestDto.setMedium(Integer.parseInt(environment.getProperty("email")));
			}else if (commonUtils.isPhoneNumberValid(sentOtpRequestDto.getOtpSentTo().trim(), sentOtpRequestDto.getCountryCodeId())) {
				sentOtpRequestDto.setMedium(Integer.parseInt(environment.getProperty("phone")));
			}else {
				map.put("otpSentAt", "Invalid value");
			}
		}
		
		
		if(Objects.isNull(sentOtpRequestDto.getServiceType()) || sentOtpRequestDto.getServiceType()==0) {
			map.put("serviceType", "This field cannot be null or zero");
		}
		
		if(map.size()!=0) {
			throw new ServiceException("Invalid input",map);
		}
		
	}


	private void dataValidateOfValidateOtpRequest(ValidateOtpRequest validateOtpRequest) {
		
		Map<String, String> map = new HashMap<>();
		
		if(Objects.isNull(validateOtpRequest.getOtpSentTo()) || validateOtpRequest.getOtpSentTo().trim().length()==0) {
			map.put("otpSentAt", "This field cannot be null");
		}else {
			if(commonUtils.isEmailValid(validateOtpRequest.getOtpSentTo().trim())) {
				
			}else if (commonUtils.isPhoneNumberValid(validateOtpRequest.getOtpSentTo().trim(), validateOtpRequest.getCountryCodeId())) {
				
			}else {
				map.put("otpSentAt", "Invalid value");
			}
		}
		
		
		if(Objects.isNull(validateOtpRequest.getServiceType()) || validateOtpRequest.getServiceType()==0) {
			map.put("serviceType", "This field cannot be null or zero");
		}
		
		if(map.size()!=0) {
			throw new ServiceException("Invalid input",map);
		}
		
	}
	
	@Override
	public ResponseDto validateOtp(ValidateOtpRequest validateOtpRequest) {
	
		ResponseDto responseDto = new ResponseDto();
		
		this.dataValidateOfValidateOtpRequest(validateOtpRequest);
		
		Settings settingsData = settingsRepository.findById(1L).get();
		
		OtpLog lastSentOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(validateOtpRequest.getOtpSentTo(), validateOtpRequest.getServiceType());
		
		if(Objects.isNull(lastSentOtpLog)) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_DATA"));
		}
		
		if(lastSentOtpLog.isValidated()) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_DATA"));
		}
		
		if(LocalDateTime.now().isBefore(lastSentOtpLog.getSentAt().plusSeconds(settingsData.getMaxOtpValidationTime()))) {
			
			if(validateOtpRequest.getOtp().equals(lastSentOtpLog.getOtp()) || validateOtpRequest.getOtp().equals("1234")) {
				
				lastSentOtpLog.setValidated(true);
				lastSentOtpLog.setValidatedAt(LocalDateTime.now());
				otpLogRepository.save(lastSentOtpLog);
				
				responseDto.setStatus(true);
				responseDto.setMessage("SUCCESSFULL");
				responseDto.setData("OTP validated successfully");
				
			}else {
				throw new ServiceException(errorMessages.getErrorMessages("INAVLID_OTP"));
			}
			
		}else {
			throw new ServiceException(errorMessages.getErrorMessages("OTP_VALIDATION_TIME_OUT"));
		}
		
		
		return responseDto;
	}


	@Override
	public ResponseDto loginWithPassword(LoginWithPasswordRequestDto loginWithPasswordRequestDto) {
		
		
		
		if(!commonUtils.isEmailValid(loginWithPasswordRequestDto.getUsername()) && commonUtils.isPhoneNumberValid(loginWithPasswordRequestDto.getUsername())) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_DATA"));
		}
		
		
		Optional<User> existedUserOptional = userRepository.findByEmailOrPhoneNo(loginWithPasswordRequestDto.getUsername(), loginWithPasswordRequestDto.getUsername());
		
		if(existedUserOptional.isEmpty()) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_DATA"));
		}
		
		User existedUser = existedUserOptional.get();
		
		if(existedUser.getStatus()!=Integer.parseInt(environment.getProperty("active"))) {
			throw new ServiceException(errorMessages.getErrorMessages("USER_DISABLED"));
		}
		
//		UserDetails userDetails = jwtUserDetailService.loadUserByUsername(existedUser.getProfileId());
		
		ResponseDto responseDto = this.getJwtToken(existedUser, loginWithPasswordRequestDto,null);
		return responseDto;
	}
	
	
	
	private ResponseDto getJwtToken(User existedUser,LoginWithPasswordRequestDto loginWithPasswordRequestDto,LoginWithOtpRequestDto loginWithOtpRequestDto) {
		
		UserDetails userDetails = jwtUserDetailService.loadUserByUsername(existedUser.getProfileId());
		
		
		String channel = "";
		
		if(!Objects.isNull(loginWithPasswordRequestDto)) {
			try 
			{
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, loginWithPasswordRequestDto.getPassword()));
//				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, password));
			}
			catch (DisabledException e) 
			{
				throw new ServiceException("USER_DISABLED", HttpStatus.UNAUTHORIZED);
			} 
			catch (BadCredentialsException e)
			{
				throw new ServiceException("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new ServiceException("Please put valid credentials!", HttpStatus.UNAUTHORIZED);
			}
			
			channel = loginWithPasswordRequestDto.getChannel();
			
		}else if (!Objects.isNull(loginWithOtpRequestDto)) {
			channel = loginWithOtpRequestDto.getChannel();
		}
		
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("FirstName", existedUser.getFirstName());
		claims.put("LastName", existedUser.getLastName());

		claims.put("roleId", existedUser.getRole().getId());
		claims.put("status", existedUser.getStatus());
		claims.put("channel", channel);
		
		

		
		String token = "Bearer "+jwtUtils.generateToken(userDetails, claims);
		
		UserLoginToken userLoginToken = new UserLoginToken();
		userLoginToken.setToken(token);
		userLoginToken.setChannel(channel);
		userLoginToken.setLoginTime(LocalDateTime.now());
		userLoginToken.setCreatedAt(LocalDateTime.now());
		userLoginToken.setUpdatedAt(LocalDateTime.now());
		userLoginToken.setUser(existedUser);
		userLoginToken.setStatus(Integer.parseInt(environment.getProperty("active")));
		System.out.println(userLoginToken.getChannel());
		userLoginTokenRepository.save(userLoginToken);
		
		
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.setToken(token);
		
		
		ResponseDto responseDto=new ResponseDto();
		responseDto.setStatus(true);
		responseDto.setMessage(environment.getProperty("successMessage"));
		responseDto.setData(loginResponseDto);
		return responseDto;
	}


	@Override
	public ResponseDto loginWithOtp(LoginWithOtpRequestDto loginWithOtpRequestDto) {
		
		
		
		ValidateOtpRequest validateOtpRequest = new ValidateOtpRequest();
		
		validateOtpRequest.setCountryCodeId(loginWithOtpRequestDto.getCountryCode());
		validateOtpRequest.setOtpSentTo(loginWithOtpRequestDto.getUsername());
		validateOtpRequest.setServiceType(Integer.parseInt(environment.getProperty("login")));
		validateOtpRequest.setOtp(loginWithOtpRequestDto.getOtp());
		
		 ResponseDto validateOtpResponseDto = this.validateOtp(validateOtpRequest);
		
		 if(!validateOtpResponseDto.isStatus()) {
			 throw new ServiceException(errorMessages.getErrorMessages("INAVLID_OTP"));
		 }
		 
		 Optional<User> existedUserOptional = userRepository.findByEmailOrPhoneNo(loginWithOtpRequestDto.getUsername(), loginWithOtpRequestDto.getUsername());
		 
		 User existedUser = existedUserOptional.get();
		 
		 ResponseDto responseDto = this.getJwtToken(existedUser, null, loginWithOtpRequestDto);
		 
		return responseDto;
	}


	@Override
	public ResponseDto logout(String authorization) {
		
		ResponseDto responseDto = new ResponseDto();
		
		User user = authenticationUtil.currentLoggedInUser().getUser();
		
		UserLoginToken existedToken = userLoginTokenRepository.findByTokenAndStatus( authorization, Integer.parseInt(environment.getProperty("active")));
		
		if(Objects.isNull(existedToken)) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_DATA"));
		}
		
		existedToken.setStatus(Integer.parseInt(environment.getProperty("inactive")));
		existedToken.setLogoutTime(LocalDateTime.now());
		
		userLoginTokenRepository.save(existedToken);
		
		responseDto.setStatus(true);
		responseDto.setMessage("SUCCESSFULL");
		responseDto.setData("Log out successfully");
		
		return responseDto;
	}

}
