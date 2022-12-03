package com.MySchool.repositories.master;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.master.Settings;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

}
