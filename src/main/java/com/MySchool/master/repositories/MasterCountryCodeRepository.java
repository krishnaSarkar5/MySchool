package com.MySchool.master.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.master.entities.MasterCountryCode;

public interface MasterCountryCodeRepository extends JpaRepository<MasterCountryCode, Long> {

	public MasterCountryCode findByIdAndStatus(Long countryCodeId,Integer status);
}
