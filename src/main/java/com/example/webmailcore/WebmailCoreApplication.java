package com.example.webmailcore;

import com.example.webmailcore.services.AirplaneCompanyService;
import com.example.webmailcore.services.GroupService;
import com.example.webmailcore.services.PrivilegeService;
import com.example.webmailcore.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@EnableCaching
public class WebmailCoreApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(WebmailCoreApplication.class, args);

        UserService userService = (UserService) context.getBean("userService");
        GroupService groupService = (GroupService) context.getBean("groupService");
        PrivilegeService privilegeService = (PrivilegeService) context.getBean("privilegeService");
        AirplaneCompanyService airplaneCompanyService = (AirplaneCompanyService) context.getBean("airplaneCompanyService");

        //TODO: fix
//        ImportData.importBasicUsersGroupsPrivilegesData(userService, groupService, privilegeService, airplaneCompanyService);

    }

}
