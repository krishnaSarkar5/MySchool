package com.MySchool.user.teacher.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.user.teacher.dto.TeacherProfileCreateRequestDto;
import com.MySchool.user.teacher.service.TeacherProfileService;

@RestController
@RequestMapping("/teacher")
public class TeacherProfileController {

	@Autowired
	private TeacherProfileService teacherProfileService;
	
	@PostMapping("/create-profile")
	public ResponseEntity<ResponseDto> createTeacherProfile(@RequestHeader("Authorization") String Authorization,@RequestBody TeacherProfileCreateRequestDto teacherProfileCreateRequestDto){
		 ResponseDto responseDto = teacherProfileService.createTeacherProfile(teacherProfileCreateRequestDto);
		 return new ResponseEntity<>(responseDto,HttpStatus.OK);
	}
}
