package com.MySchool.entities.user;


import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.management.ConstructorParameters;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.MySchool.entities.master.Role;
import com.MySchool.master.entities.MasterCountryCode;
import com.MySchool.user.registration.dto.RegistrationRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "firstName")
	private String firstName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Column(name="phone")
	private String phoneNo;
	
	@Column(name="profile_id")
	private String profileId;
	
	@ManyToOne
	@JoinColumn(name = "country_code_id")
	private MasterCountryCode masterCountryCode;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;
	
	
	public User(RegistrationRequestDto registrationRequestDto) {
		this.firstName=registrationRequestDto.getFirstName();
		this.lastName=registrationRequestDto.getLastName();
		this.email=registrationRequestDto.getEmail();
		this.phoneNo=registrationRequestDto.getPhoneNo();
		this.password=registrationRequestDto.getPassword();
		this.createdAt=LocalDateTime.now();
		
	}
}

