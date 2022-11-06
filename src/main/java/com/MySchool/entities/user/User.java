package com.MySchool.entities.user;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.MySchool.entities.Role;
import com.MySchool.master.entities.MasterCountryCode;

import lombok.Data;

@Data
@Entity
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
}

