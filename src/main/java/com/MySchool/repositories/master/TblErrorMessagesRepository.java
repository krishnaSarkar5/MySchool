package com.MySchool.repositories.master;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.master.TblErrorMessages;



public interface TblErrorMessagesRepository extends JpaRepository<TblErrorMessages, Long> {

	TblErrorMessages findByErrorCode(String errorCode);
	
	TblErrorMessages findFirstByErrorCodeOrderByCreatedAtDesc(String errorCode);

}
