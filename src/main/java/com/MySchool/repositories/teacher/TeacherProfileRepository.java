package com.MySchool.repositories.teacher;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.teacher.TeacherProfile;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {

}
