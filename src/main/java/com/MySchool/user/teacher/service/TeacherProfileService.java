package com.MySchool.user.teacher.service;

import com.MySchool.dto.authenticationDto.ResponseDto;
import com.MySchool.user.teacher.dto.TeacherProfileCreateRequestDto;

public interface TeacherProfileService {

	public ResponseDto createTeacherProfile(TeacherProfileCreateRequestDto teacherProfileCreateRequestDto);
}
