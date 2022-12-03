package com.MySchool.entities.teacher;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.MySchool.entities.master.MasterClass;
import com.MySchool.entities.master.MasterQualification;
import com.MySchool.entities.master.MasterSubject;
import com.MySchool.entities.user.User;

import lombok.Data;

@Data
@Entity
public class TeacherProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToMany
	private List<MasterClass> classList;
	
	
	@ManyToMany
	private List<MasterQualification> qualifications;
	
	@ManyToMany
	private List<MasterSubject> subjects;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	

	
	
}
