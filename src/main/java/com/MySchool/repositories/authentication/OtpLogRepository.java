package com.MySchool.repositories.authentication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.authentication.OtpLog;

public interface OtpLogRepository extends JpaRepository<OtpLog, Long> {

	public OtpLog findFirstByOtpViaOrderBySentAtDesc(String otpViaValue);
	
	
	public OtpLog findFirstByOtpViaAndServiceTypeOrderBySentAtDesc(String otpViaValue,Integer serviceType);
	
	public OtpLog findFirstByOtpViaAndServiceTypeInOrderBySentAtDesc(String otpViaValue,List<Integer> serviceType);
}
