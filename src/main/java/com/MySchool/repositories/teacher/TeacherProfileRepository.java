package com.MySchool.repositories.teacher;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.teacher.TeacherProfile;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {

	public Optional<TeacherProfile> findByUser_idAndUser_journeyStatus(Long userId,Integer journeyStatus);
}
