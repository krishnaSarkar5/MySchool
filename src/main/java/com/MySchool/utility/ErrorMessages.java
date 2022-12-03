package com.MySchool.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.MySchool.entities.master.TblErrorMessages;
import com.MySchool.repositories.master.TblErrorMessagesRepository;



@Component
public class ErrorMessages {
	
	@Autowired
	private TblErrorMessagesRepository tblErrorMessagesRepository;
	
	public String getErrorMessages(String errorCode) {

		// to get the object with errorCode
		TblErrorMessages tblErrorMessage = tblErrorMessagesRepository.findFirstByErrorCodeOrderByCreatedAtDesc(errorCode);
		
		// if condition for validate data
		if (tblErrorMessage != null) {
			return tblErrorMessage.getErrorMessage();
		} else {
			return null;
		}

	}

}
