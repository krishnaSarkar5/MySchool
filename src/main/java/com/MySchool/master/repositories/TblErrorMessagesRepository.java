package com.MySchool.master.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.master.entities.TblErrorMessages;



public interface TblErrorMessagesRepository extends JpaRepository<TblErrorMessages, Long> {

	TblErrorMessages findByErrorCode(String errorCode);
	
	TblErrorMessages findFirstByErrorCodeOrderByCreatedAtDesc(String errorCode);

}
