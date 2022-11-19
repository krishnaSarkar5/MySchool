package com.MySchool.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MySchool.entities.user.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	
	Optional<User> findByEmailOrPhoneNo(String emailOrPhone1,String emailOrPhone2);
	
	User findByProfileId(String profileId);
}
