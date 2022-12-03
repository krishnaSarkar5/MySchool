package com.MySchool.repositories.master;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.master.MasterCountryCode;

public interface MasterCountryCodeRepository extends JpaRepository<MasterCountryCode, Long> {

	public MasterCountryCode findByIdAndStatus(Long countryCodeId,Integer status);
}
