package com.MySchool.repositories.master;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.master.MasterQualification;

public interface MasterQualificationRepositry extends JpaRepository<MasterQualification, Long> {

	public List<MasterQualification> findByIdInAndStatus(List<Long> idList,Integer status);
}
