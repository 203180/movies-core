package com.example.webmailcore.services;
import com.example.webmailcore.models.AirplaneCompany;
import com.example.webmailcore.models.Group;
import com.example.webmailcore.models.Privilege;
import com.example.webmailcore.models.User;
import com.example.webmailcore.repositories.GroupRepository;
import com.example.webmailcore.repositories.PrivilegeRepository;
import com.example.webmailcore.repositories.UserRepository;
import com.example.webmailcore.repositories.specifications.GroupSpecification;
import com.example.webmailcore.repositories.specifications.SearchCriteria;
import com.example.webmailcore.repositories.specifications.SearchOperation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    public Page<Group> getAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    public Page<Group> all(Map<String, String> params, Pageable pageable) {
        GroupSpecification groupSpecification = new GroupSpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (!StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue())) {
                groupSpecification.add(new SearchCriteria(entry.getKey(), entry.getValue(), SearchOperation.MATCH));
            }
        }
        return groupRepository.findAll(groupSpecification, pageable);
    }

    public Group getById(String id) {
        Group group = groupRepository.getById(id);
        Hibernate.initialize(group.getUsers());
        group.setMembers(group.getUsers());
        Hibernate.initialize(group.getPrivileges());
        return group;
    }

    public Boolean exists(String id) {
        return groupRepository.existsById(id);
    }

    public Group findGroupByCode(String code) {
        return groupRepository.findByCode(code);
    }

    public Group findByAirplaneCompanyIdAndCodeEndsWith(String airplaneCompanyId, String code) {
        return groupRepository.findByAirplaneCompanyIdAndCodeEndsWith(airplaneCompanyId, code);
    }

    public Group create(Group group) {
        if (group.getId() != null) {
            Group tmp = groupRepository.getById(group.getId());
            group.setUsers(tmp.getUsers());
        }
        return groupRepository.save(group);
    }

    public Group update(Group group) {
        return groupRepository.save(group);
    }

    public Boolean delete(String id) {
        Group group = groupRepository.getById(id);
        groupRepository.delete(group);
        return true;
    }

    @Transactional
    public Group addPrivilege(String groupId, Privilege privilege) {
        Group group = groupRepository.findById(groupId).orElse(null);
        group.getPrivileges().add(privilege);
        return groupRepository.save(group);
    }

    @Transactional
    public Group removePrivilege(String groupId, Privilege privilege) {
        Group group = groupRepository.findById(groupId).orElse(null);
        Privilege privilegeToRemove = group.getPrivileges().stream().filter(privilege1 -> privilege1.getId().equals(privilege.getId())).findFirst().orElse(null);
        group.getPrivileges().remove(privilegeToRemove);
        return groupRepository.save(group);
    }

    @Transactional
    public Boolean addUserToGroup(String groupId, String userId) {
        Group group = groupRepository.getById(groupId);
        User user = userRepository.getById(userId);
        if (!user.getGroups().contains(group)) {
            user.getGroups().add(group);
            userRepository.save(user);
        }
        return Boolean.TRUE;
    }

    @Transactional
    public Group addGroupToAirplaneCompany(String groupId, String orgId) {
        AirplaneCompany airplaneCompany = new AirplaneCompany();
        airplaneCompany.setId(orgId);
        Group group = groupRepository.getById(groupId);
        group.setAirplaneCompany(airplaneCompany);
        return groupRepository.save(group);
    }

    @Transactional
    public Boolean removeUserFromGroup(String groupId, String userId) {
        Group group = groupRepository.getById(groupId);
        User user = userRepository.getById(userId);
        user.getGroups().remove(group);
        userRepository.save(user);
        return Boolean.TRUE;
    }

    public List<Group> getUserGroupByAirplaneCompany(AirplaneCompany airplaneCompany) {
        GroupSpecification specification = new GroupSpecification();
        specification.add(new SearchCriteria("airplaneCompany.id", airplaneCompany.getId(), SearchOperation.EQUAL));
        specification.add(new SearchCriteria("code", "-CLIENT", SearchOperation.MATCH_END));
        return groupRepository.findAll(specification);
    }

    public List<Group> getAdminGroupByAirplaneCompany(AirplaneCompany airplaneCompany) {
        GroupSpecification specification = new GroupSpecification();
        specification.add(new SearchCriteria("airplaneCompany.id", airplaneCompany.getId(), SearchOperation.EQUAL));
        specification.add(new SearchCriteria("code", "-ADMIN", SearchOperation.MATCH_END));
        return groupRepository.findAll(specification);
    }
}
