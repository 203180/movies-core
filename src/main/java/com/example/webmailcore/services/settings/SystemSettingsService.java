package com.example.webmailcore.services.settings;

import com.example.webmailcore.auth.CustomUserDetails;
import com.example.webmailcore.models.settings.SystemSettingsProp;
import com.example.webmailcore.repositories.settings.SystemSettingsRepository;
import com.example.webmailcore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemSettingsService {

    @Autowired
    SystemSettingsRepository repo;

    @Autowired
    UserService userService;

    public SystemSettingsProp updateSettingsProp(String key, String value, CustomUserDetails userDetails){
        SystemSettingsProp prop = repo.findFirstByKey(key);
        if(prop == null){
            prop = new SystemSettingsProp();
            prop.setValue(value);
            prop.setKey(key);
        }
        if(userDetails != null) {
            prop.setModifiedBy(userService.get(userDetails.getUserId()));
        }
        prop.setValue(value);
        return repo.save(prop);
    }

    public SystemSettingsProp getSystemSettingsPropByKey(String key){
        return repo.findFirstByKey(key);
    }

    public List<SystemSettingsProp> getAllSettings(){
        return repo.findAllByKeyContainsOrderByKey("");
    }
}

