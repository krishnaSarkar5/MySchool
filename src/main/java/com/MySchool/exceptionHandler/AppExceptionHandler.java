package com.MySchool.exceptionHandler;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.MySchool.dto.authenticationDto.ErrorMessage;
import com.MySchool.exception.ServiceException;
import com.MySchool.utility.ErrorMessages;



//@RestControllerAdvice
public class AppExceptionHandler {

	@Autowired
	private ErrorMessages errorMessages;

	@ExceptionHandler(value = ServiceException.class)
	public ResponseEntity<Object> handleServiceException(ServiceException ex){
		
		
		String erroMessageDescription = ex.getMessage();
	
		ErrorMessage errorMessage = new ErrorMessage(erroMessageDescription, ex.getIndexError());
		
		return new ResponseEntity<Object>(errorMessage,new HttpHeaders(),ex.getStatus());
		
	}
	
	
	@ExceptionHandler(value= MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,WebRequest request){
		
		Map<String, String> resp = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach( (error)->{
			
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			
			resp.put(fieldName, message);
			
			});
		return new ResponseEntity<Map<String, String>>(resp,new HttpHeaders(),HttpStatus.BAD_REQUEST);
		
	}
	
	
	
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex){
		
		
//		String erroMessageDescription = ex.getLocalizedMessage();
		String erroMessageDescription = errorMessages.getErrorMessages("PROCESS_ERROR");
		
//		if(erroMessageDescription == null)
//			erroMessageDescription = ex.toString();
		
		ErrorMessage errorMessage = new ErrorMessage(erroMessageDescription, null);
		
		return new ResponseEntity<Object>(errorMessage,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
}
