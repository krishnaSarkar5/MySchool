package com.MySchool.user.teacher.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.entities.master.MasterClass;
import com.MySchool.entities.master.MasterQualification;
import com.MySchool.entities.master.MasterSubject;
import com.MySchool.entities.teacher.TeacherProfile;
import com.MySchool.entities.user.User;
import com.MySchool.exception.ServiceException;
import com.MySchool.repositories.UserRepository;
import com.MySchool.repositories.master.MasterClassRepository;
import com.MySchool.repositories.master.MasterQualificationRepositry;
import com.MySchool.repositories.master.MasterSubjectRepository;
import com.MySchool.repositories.teacher.TeacherProfileRepository;
import com.MySchool.user.teacher.dto.TeacherProfileCreateRequestDto;
import com.MySchool.user.teacher.service.TeacherProfileService;
import com.MySchool.utility.ErrorMessages;


@Service
public class TeacherProfileServiceImpl implements TeacherProfileService {

	@Autowired
	private Environment environment;
	
	@Autowired
	private ErrorMessages errorMessages;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TeacherProfileRepository teacherProfileRepository;
	
	@Autowired
	private MasterQualificationRepositry masterQualificationRepositry;
	
	@Autowired
	private MasterSubjectRepository masterSubjectRepository;
	
	@Autowired
	private MasterClassRepository masterClassRepository;
	
	
	@Override
	public ResponseDto createTeacherProfile(TeacherProfileCreateRequestDto teacherProfileCreateRequestDto) {
		
		this.dataValidationForTeacherProfileCreateRequestDto(teacherProfileCreateRequestDto);
		
		
		this.saveTeacherProfile(teacherProfileCreateRequestDto);
		
		ResponseDto responseDto = new ResponseDto();
		responseDto.setData("Teacher Profile Created");
		responseDto.setMessage("SUCCESSFUL");
		responseDto.setStatus(true);
		
		return responseDto;
	}
	
	
	private void saveTeacherProfile(TeacherProfileCreateRequestDto teacherProfileCreateRequestDto) {
		
		Optional<TeacherProfile> existedTeacherProfileOptional = teacherProfileRepository.findByUser_idAndUser_journeyStatus(teacherProfileCreateRequestDto.getUserId(), Integer.parseInt(environment.getProperty("registrationCompleted")));
		
		if(existedTeacherProfileOptional.isEmpty()) {
			throw new ServiceException(errorMessages.getErrorMessages("NO_USER_FOUND"));
		}
		
		TeacherProfile existedTeacherProfile = existedTeacherProfileOptional.get();
		
		User existedTeacher = existedTeacherProfile.getUser();
		
		
		List<MasterQualification> existedMasterQualificationList = masterQualificationRepositry.findByIdInAndStatus(teacherProfileCreateRequestDto.getQualifications(),Integer.parseInt(environment.getProperty("active")));
		
		List<MasterSubject> existedMasterSubjectList = masterSubjectRepository.findByIdInAndStatus(teacherProfileCreateRequestDto.getSubjects(), Integer.parseInt(environment.getProperty("active")));
		
		List<MasterClass> exiatedMasterClassList = masterClassRepository.findByIdInAndStatus(teacherProfileCreateRequestDto.getClasses(), Integer.parseInt(environment.getProperty("active")));
		
		
		if(Objects.isNull(existedMasterQualificationList) ||  existedMasterQualificationList.size()!=teacherProfileCreateRequestDto.getQualifications().size()) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_QUALIFICATION_DATA"));
		}
		
		if(Objects.isNull(existedMasterSubjectList) || existedMasterSubjectList.size()!=teacherProfileCreateRequestDto.getSubjects().size()) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_SUBJECT_DATA"));
		}
		
		if(Objects.isNull(exiatedMasterClassList) || exiatedMasterClassList.size()!=teacherProfileCreateRequestDto.getClasses().size()) {
			throw new ServiceException(errorMessages.getErrorMessages("INVALID_CLASS_DATA"));
		}
		
		existedTeacherProfile.setQualifications(existedMasterQualificationList);
		existedTeacherProfile.setSubjects(existedMasterSubjectList);
		existedTeacherProfile.setClassList(exiatedMasterClassList);
		
		existedTeacherProfile.setUpdatedAt(LocalDateTime.now());
		
		teacherProfileRepository.save(existedTeacherProfile);
		
		
	}

	
	private void dataValidationForTeacherProfileCreateRequestDto(TeacherProfileCreateRequestDto teacherProfileCreateRequestDto) {
		
		Map<String, String> errorMap =  new HashMap<>();
		
		if(Objects.isNull(teacherProfileCreateRequestDto)) {
			errorMap.put("Request Body", errorMessages.getErrorMessages("NULL_REQUEST"));
		}
		else {
			if (Objects.isNull(teacherProfileCreateRequestDto.getUserId()) || teacherProfileCreateRequestDto.getUserId()==0L) {
				errorMap.put("User id", errorMessages.getErrorMessages("INVALID_DATA"));
			}
			
			
			if (Objects.isNull(teacherProfileCreateRequestDto.getSubjects()) || teacherProfileCreateRequestDto.getSubjects().size()==0) {
				errorMap.put("Subject list", errorMessages.getErrorMessages("NULL_REQUEST"));
			}
			
			if (Objects.isNull(teacherProfileCreateRequestDto.getClasses()) || teacherProfileCreateRequestDto.getClasses().size()==0) {
				errorMap.put("Classes list", errorMessages.getErrorMessages("NULL_REQUEST"));
			}
			
			if (Objects.isNull(teacherProfileCreateRequestDto.getQualifications()) || teacherProfileCreateRequestDto.getQualifications().size()==0) {
				errorMap.put("Qualification list", errorMessages.getErrorMessages("NULL_REQUEST"));
			}
		}
		
		
		if(errorMap.size()!=0) {
			throw new ServiceException("Invalid Data",errorMap);
		}
		
	}
}
