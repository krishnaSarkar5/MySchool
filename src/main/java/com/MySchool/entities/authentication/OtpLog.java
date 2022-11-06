package com.MySchool.entities.authentication;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.MySchool.master.entities.MasterCountryCode;

import lombok.Data;

@Data
@Entity
@Table(name="otp_log")
public class OtpLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "otp")
	private String otp;
	
	@Column(name = "otp_via")
	private String otpVia;
	
	// email =1 phone =2
	@Column(name = "otp_medium")
	private Integer otpMedium;
	
	@Column(name = "validated")
	private boolean validated;
	
	@Column(name = "sent_at")
	private LocalDateTime sentAt;
	
	@Column(name = "validated_at")
	private LocalDateTime validatedAt;
	
	@Column(name = "count")
	private Integer count;
	
	// signup=1,login=2,change email/phone=3
	@Column(name = "service_type")
	private Integer serviceType;
	
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "validation_window_time")
	private LocalDateTime validationWindowTime;
	
	
	@ManyToOne
	@JoinColumn(name="country_code_id")
	private MasterCountryCode masterCountryCode;
}
