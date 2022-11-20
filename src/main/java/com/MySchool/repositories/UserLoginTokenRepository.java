package com.MySchool.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.user.UserLoginToken;

public interface UserLoginTokenRepository extends JpaRepository<UserLoginToken, Long> {

	UserLoginToken findByUser_idAndTokenAndStatus(Long id, String requestTokenHeader, int i);
	
	UserLoginToken findByTokenAndStatus( String requestTokenHeader, int i);

}
