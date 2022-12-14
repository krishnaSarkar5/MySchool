package com.MySchool.dto.authenticationDto;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	
	@NonNull
	private String username;
	
	@Nullable
	private String password;
	

	Long PhoneCodeId;
	
	@NonNull
	String channel;

}
