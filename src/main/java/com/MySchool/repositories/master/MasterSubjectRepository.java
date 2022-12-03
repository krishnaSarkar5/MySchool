package com.MySchool.repositories.master;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.master.MasterSubject;

public interface MasterSubjectRepository extends JpaRepository<MasterSubject, Long> {

	public List<MasterSubject> findByIdInAndStatus(List<Long> idList,Integer status);
}
