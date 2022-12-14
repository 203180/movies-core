package com.example.webmailcore;

import com.example.webmailcore.models.AirplaneCompany;
import com.example.webmailcore.models.Group;
import com.example.webmailcore.models.Privilege;
import com.example.webmailcore.models.User;
import com.example.webmailcore.services.AirplaneCompanyService;
import com.example.webmailcore.services.GroupService;
import com.example.webmailcore.services.PrivilegeService;
import com.example.webmailcore.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class ImportData {

    public static void importBasicUsersGroupsPrivilegesData(
            UserService userService,
            GroupService groupService,
            PrivilegeService privilegeService,
            AirplaneCompanyService airplaneCompanyService
    ){
        Privilege privilegeAdministration = privilegeService.findByName("ADMINISTRATION");
        if (privilegeAdministration == null) {
            Privilege privilege = new Privilege();
            privilege.setName("ADMINISTRATION");
            privilegeAdministration = privilegeService.save(privilege);
        }

        Privilege privilegeClient = privilegeService.findByName("CLIENT");
        if (privilegeClient == null) {
            Privilege privilege = new Privilege();
            privilege.setName("CLIENT");
            privilegeClient = privilegeService.save(privilege);
        }

        Privilege privilegeAgent = privilegeService.findByName("ASTRA_AIR_AGENT");
        if (privilegeAgent == null) {
            Privilege privilege = new Privilege();
            privilege.setName("ASTRA_AIR_AGENT");
            privilegeAgent = privilegeService.save(privilege);
        }

        Group astraAirAdminGroup = groupService.findGroupByCode("ASTRA_AIR_ADMIN");
        if (astraAirAdminGroup == null) {
            Group group = new Group();
            group.setCode("ASTRA_AIR_ADMIN");
            group.setName("Administrators - Astra Air");
            astraAirAdminGroup = groupService.create(group);
            groupService.addPrivilege(astraAirAdminGroup.getId(), privilegeAdministration);
        }

        Group astraAirClientGroup = groupService.findGroupByCode("ASTRA_AIR_CLIENT");
        if (astraAirClientGroup == null) {
            Group group = new Group();
            group.setCode("ASTRA_AIR_CLIENT");
            group.setName("Clients - Аста Адриа");
            astraAirClientGroup = groupService.create(group);
//            groupService.addPrivilege(astraAirClientGroup.getId(), privilegeClient);
        }

        AirplaneCompany astraAirOrg = airplaneCompanyService.findByNameEn("ASTRA AIR");
        if (astraAirOrg == null) {
            AirplaneCompany airplaneCompany = new AirplaneCompany();
            airplaneCompany.setName("Astra Air");
            astraAirOrg = airplaneCompanyService.save(airplaneCompany);
            groupService.addGroupToAirplaneCompany(astraAirAdminGroup.getId(), astraAirOrg.getId());
            groupService.addGroupToAirplaneCompany(astraAirClientGroup.getId(), astraAirOrg.getId());
            groupService.addGroupToAirplaneCompany(astraAirClientGroup.getId(), astraAirOrg.getId());
        }

        User rootUser = userService.getUserByUsername("root");
        if (rootUser == null) {
            rootUser = new User();
            rootUser.setEmail("devtest@aucta.dev");
            rootUser.setFirstName("Root");
            rootUser.setLastName("User");
            rootUser.setUsername("root");
            rootUser.setPassword("pass123");
            rootUser.setAirplaneCompany(astraAirOrg);
            rootUser = userService.save(rootUser);
            groupService.addUserToGroup(astraAirAdminGroup.getId(), rootUser.getId());
        }
    }
}
