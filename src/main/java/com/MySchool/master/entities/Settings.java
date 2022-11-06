package com.MySchool.master.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "settings")
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "max_otp_attempts")
	private Integer maxOtpAttemps;
	
	// in seconds
	@Column(name = "max_otp_validation_time")
	private Long maxOtpValidationTime;
	
	@Column(name = "window_duration")
	private Long windowDuration;
	
	
}
