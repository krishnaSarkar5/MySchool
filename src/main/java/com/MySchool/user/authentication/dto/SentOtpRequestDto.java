package com.MySchool.user.authentication.dto;

import javax.validation.constraints.NotNull;

import com.MySchool.master.repositories.TblErrorMessagesRepository;
import com.MySchool.utility.CommonUtils;
import com.MySchool.utility.ErrorMessages;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Data
public class SentOtpRequestDto {

	@NotNull(message = "otpSentAt can not be null")
	private String otpSentTo;
	
	private Long countryCodeId;
	
	@NotNull(message = "serviceType can not be null")
	private Integer serviceType;
	
	@JsonIgnore
	private Integer medium;
	
	
	
}
