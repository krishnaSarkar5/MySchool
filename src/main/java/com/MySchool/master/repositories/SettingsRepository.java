package com.MySchool.master.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.master.entities.Settings;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

}
