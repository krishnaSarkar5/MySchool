package com.MySchool.user.teacher.dto;

import java.util.List;

import lombok.Data;

@Data
public class TeacherProfileCreateRequestDto {

	private Long userId;
	
	private List<Long> qualifications;
	
	private List<Long> subjects;
	
	private List<Long> classes;
}
