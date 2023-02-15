package com.example.webmailcore.services;

import com.example.webmailcore.enums.AirplaneCompanyType;
import com.example.webmailcore.models.AirplaneCompany;
import com.example.webmailcore.models.Country;
import com.example.webmailcore.models.Group;
import com.example.webmailcore.models.Privilege;
import com.example.webmailcore.repositories.AirplaneCompanyRepository;
import com.example.webmailcore.repositories.specifications.AirplaneCompanySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AirplaneCompanyService {

    @Autowired
    AirplaneCompanyRepository repository;

    @Autowired
    PrivilegeService privilegeService;

    @Autowired
    GroupService groupService;

    public List<AirplaneCompany> getAll() {
        return repository.findAll();
    }

    public Page<AirplaneCompany> all(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<AirplaneCompany> all(Map<String, String> params, Pageable pageable) {
        AirplaneCompanySpecification airplaneCompanySpecification = new AirplaneCompanySpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(airplaneCompanySpecification, pageable);
    }

//    public List<AirplaneCompany> all() {
//        List<AirplaneCompany> airplaneCompanies = new ArrayList<>();
//        return airplaneCompanies.stream().map(AirplaneCompany::toAirplaneCompanyDTO).collect(Collectors.toList());
//    }


    public AirplaneCompany findByNameEn(String nameEn) {
        return repository.findByName(nameEn);
    }

    public AirplaneCompany save(AirplaneCompany airplaneCompany) {
        return repository.save(airplaneCompany);
    }

    public AirplaneCompany create(AirplaneCompany airplaneCompany) {
//        if (airplaneCompany.getType().equals(AirplaneCompanyType.CLIENT)) {
//            Privilege clientAdminRole = privilegeService.findByName("CLIENT_ADMIN");
//            Privilege clientRole = privilegeService.findByName("CLIENT");
//            airplaneCompany = repository.save(airplaneCompany);
//            createAdminGroupForOrg(airplaneCompany, clientAdminRole);
//            createClientGroupForOrg(airplaneCompany, clientRole);
//        } else {
            airplaneCompany = repository.save(airplaneCompany);
//        }
        return airplaneCompany;
    }

    @Async
    public void createAdminGroupForOrg(AirplaneCompany a, Privilege clientAdminRole) {
        Group adminGroup = new Group();
        adminGroup.setName(a.getName() + " Admin Group");
        adminGroup.setCode(a.getName().toUpperCase().trim().replaceAll(" ", "-") + "-ADMIN");
        adminGroup.setAirplaneCompany(a);
        adminGroup.setPrivileges(new ArrayList<>());
        adminGroup.getPrivileges().add(clientAdminRole);
        groupService.create(adminGroup);
    }

    @Async
    public void createClientGroupForOrg(AirplaneCompany a, Privilege clientRole) {
        Group clientGroup = new Group();
        clientGroup.setName(a.getName() + " Client Group");
        clientGroup.setCode(a.getName().toUpperCase().trim().replaceAll(" ", "-") + "-CLIENT");
        clientGroup.setAirplaneCompany(a);
        clientGroup.setPrivileges(new ArrayList<>());
        clientGroup.getPrivileges().add(clientRole);
        groupService.create(clientGroup);
    }
}
