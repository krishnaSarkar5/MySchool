package com.MySchool.user.authentication.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.entities.authentication.OtpLog;
import com.MySchool.exception.ServiceException;
import com.MySchool.master.entities.MasterCountryCode;
import com.MySchool.master.entities.Settings;
import com.MySchool.master.repositories.MasterCountryCodeRepository;
import com.MySchool.master.repositories.SettingsRepository;
import com.MySchool.repositories.authentication.OtpLogRepository;
import com.MySchool.user.authentication.dto.SentOtpRequestDto;
import com.MySchool.user.authentication.service.AuthenticationService;
import com.MySchool.utility.CommonUtils;
import com.MySchool.utility.ErrorMessages;

import io.swagger.models.auth.In;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

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
	
	@Override
	public ResponseDto sentOtp(SentOtpRequestDto sentOtpRequestDto) {
		
		ResponseDto responseDto = new ResponseDto();
		
		OtpLog newOtpLog = null;
		
		Settings settingsData = settingsRepository.findById(1L).get();
		
		OtpLog lastLoginOtpLog = null;
		
		OtpLog lastSignupOtpLog = null;
		
		this.validateSentOtpRequestDto(sentOtpRequestDto);
		
		if(sentOtpRequestDto.getServiceType()==Integer.parseInt(environment.getProperty("signup"))){
			lastSignupOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(sentOtpRequestDto.getOtpSentAt(),sentOtpRequestDto.getServiceType());
		}else if(sentOtpRequestDto.getServiceType()==Integer.parseInt(environment.getProperty("login"))){
			lastLoginOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(sentOtpRequestDto.getOtpSentAt(),sentOtpRequestDto.getServiceType());
			lastSignupOtpLog = otpLogRepository.findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(sentOtpRequestDto.getOtpSentAt(),sentOtpRequestDto.getServiceType());
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
			
			// chech current time exceed window time or not
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
		newOtpLog.setOtpVia(sentOtpRequestDto.getOtpSentAt());
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
	
	
	private void validateSentOtpRequestDto(SentOtpRequestDto sentOtpRequestDto) {
		
		Map<String, String> map = new HashMap<>();
		
		if(Objects.isNull(sentOtpRequestDto.getOtpSentAt()) || sentOtpRequestDto.getOtpSentAt().trim().length()==0) {
			map.put("otpSentAt", "This field cannot be null");
		}else {
			if(CommonUtils.isEmailValid(sentOtpRequestDto.getOtpSentAt().trim())) {
				sentOtpRequestDto.setMedium(Integer.parseInt(environment.getProperty("email")));
			}else if (CommonUtils.isPhoneNumberValid(sentOtpRequestDto.getOtpSentAt().trim(), sentOtpRequestDto.getCountryCodeId())) {
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

}
