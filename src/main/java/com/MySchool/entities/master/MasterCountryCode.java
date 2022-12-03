package com.MySchool.entities.master;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "master_country_code")
public class MasterCountryCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "country_code")
	private Long countryCode;
	
	@Column(name="country_name")
	private String countryName;
	
	@Column(name="status")
	private Integer status;
}
