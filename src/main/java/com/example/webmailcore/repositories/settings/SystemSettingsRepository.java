package com.example.webmailcore.repositories.settings;

import com.example.webmailcore.models.settings.SystemSettingsProp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemSettingsRepository extends JpaRepository<SystemSettingsProp, String> {
    SystemSettingsProp findFirstByKey(String key);

    List<SystemSettingsProp> findAllByKeyContainsOrderByKey(String key);
}

