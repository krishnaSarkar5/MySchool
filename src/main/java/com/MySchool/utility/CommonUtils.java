package com.MySchool.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.MySchool.entities.user.User;
import com.MySchool.exception.ServiceException;
import com.MySchool.master.entities.MasterCountryCode;

@Component
public class CommonUtils {

	@Autowired
	private static ErrorMessages errorMessages;
	
	@Autowired
	private Environment environment;
	
	
	public  boolean isEmailValid(String emailAddress) {
		
//		ErrorMessages errorMessages = new ErrorMessages();
		 
	 	if(Objects.isNull(emailAddress)) {
	 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_EMAIL_ADDRESS"));
	 	}else if (emailAddress.trim().length()==0) {
	 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_EMAIL_ADDRESS"));
		}
		
		
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
		        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";		
		 boolean matched = Pattern.compile(regexPattern).matcher(emailAddress).matches();
	
		 
		 return matched;
	}
	
	
	 public  boolean isCountryCodeValid(String str)
	    {
//		 	ErrorMessages errorMessages = new ErrorMessages();
		 
		 	if(Objects.isNull(str)) {
		 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_COUNTRY_CODE"));
		 	}else if (str.trim().length()==0) {
		 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_COUNTRY_CODE"));
			}
		 
	        // Regex to check string contains only digits
	        String regex = "[0-9+-]+";
	        Pattern p = Pattern.compile(regex);
	        

	        Matcher m = p.matcher(str);
	        System.out.println(str+" "+m.matches());
	        
	        
	        return m.matches();
	    }
	    
	    public  boolean isPhoneNumberValid(String str)
	    {
	    	
//	    	ErrorMessages errorMessages = new ErrorMessages();
			 
		 	if(Objects.isNull(str)) {
		 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_PHONE_NUMBER"));
		 	}else if (str.trim().length()==0) {
		 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_PHONE_NUMBER"));
			}
		 
	    	
	        // Regex to check string contains only digits
	        String regex = "[0-9]+";
	        Pattern p = Pattern.compile(regex);
	        
	        if (!(!Objects.isNull(str) && !str.isBlank())) {
	            return false;
	        }
	        Matcher m = p.matcher(str);
	        
	        return m.matches();
	    }
	    
	    
	    public  boolean isPhoneNumberValid(String str,Long countryCode)
	    {
	    	
	    	
			 
		 	if(Objects.isNull(str)) {
		 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_PHONE_NUMBER"));
		 	}else if (str.trim().length()==0) {
		 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_PHONE_NUMBER"));
			}
		 
		 	if(Objects.isNull(countryCode)) {
		 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_COUNTRY_CODE"));
		 	}else if (countryCode==0l) {
		 		throw new ServiceException(errorMessages.getErrorMessages("INVALID_COUNTRY_CODE"));
			}
	    	
	        // Regex to check string contains only digits
	        String regex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" 
	        	      + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$" 
	        	      + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
	        Pattern p = Pattern.compile(regex);
	        
	        if (!(!Objects.isNull(str) && !str.isBlank())) {
	            return false;
	        }
	        Matcher m = p.matcher(str);
	        
	        return m.matches();
	    }
	    
//	    public static boolean isValidData(Object data,String fieldName) {
//	    	
//	    	Map<String,String> map = new HashMap<>();
//	    	
//	    	if(Objects.isNull(data)) {
//	    		map.put(fieldName, "Can no be null");
//	    	}else if(data instanceof String){
//	    		String str = (String) data;
//	    		if(str.trim().length()==0) {
//	    			map.put(fieldName, "Can no be empty");
//	    		}
//			}else if(data instanceof Long){
//				Long v = (Long) data;
//	    		if(v==0l) {
//	    			map.put(fieldName, "Can no be 0");
//	    		}
//			}
//	    	
//	    	if(map)
//	    	
//	    	return true;
//	    }
	    
	    public  String generateOtp(int length) {
	    	String numbers = "1234567890";
	        Random random = new Random();
	        char[] otp = new char[length];

	        for(int i = 0; i< length ; i++) {
	           otp[i] = numbers.charAt(random.nextInt(numbers.length()));
	        }
	        return new String(otp);
	    }
	    
	    public  boolean isValidPassword(String password) {
	    	
	    	 String regex = "^(?=.*[0-9])"
                     + "(?=.*[a-z])(?=.*[A-Z])"
                     + "(?=.*[@#$%^&+=])"
                     + "(?=\\S+$).{8,20}$";

     
	    	 Pattern p = Pattern.compile(regex);

      
	    	 if (password == null) {
	    		 return false;
	    	 }

      
	    	 Matcher m = p.matcher(password);

	    	return m.matches();
	    	
	    }
	    
	    public  String generateProfileId(User user) {
	    	
	    	String profileId = "";
	    	
	    	int size = Integer.parseInt(environment.getProperty("sizeOfProfileId"));
	    	
	    	String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	    	         + "0123456789"
	    	         + "abcdefghijklmnopqrstuvxyz";
	    	 
	    	  StringBuilder sb = new StringBuilder(size);	    	 
	    	  for (int i = 0; i < size; i++) {
	    	  
	    	   int index
	    	    = (int)(AlphaNumericString.length()
	    	      * Math.random());
	    		    	  
	    	   sb.append(AlphaNumericString
	    	      .charAt(index));
	    	  }
	    	 
	    	  if(user.getRole().getId()==Integer.parseInt(environment.getProperty("teacher"))) {
	    		  
	    		  profileId = "MST-"+sb.toString();
	    		  
	    	  }else if (user.getRole().getId()==Integer.parseInt(environment.getProperty("student"))) {
				
	    		  profileId = "MSS-"+sb.toString();
			}
	    	  
	    	  
	    	  return profileId;
	
	    }

}
