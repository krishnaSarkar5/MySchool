package com.MySchool.repositories.master;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.master.MasterClass;

public interface MasterClassRepository extends JpaRepository<MasterClass, Long> {

	public List<MasterClass> findByIdInAndStatus(List<Long> idList,Integer status);
}
