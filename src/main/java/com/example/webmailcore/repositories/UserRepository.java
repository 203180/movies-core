package com.example.webmailcore.repositories;

import com.example.webmailcore.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    @Override
    Page<User> findAll(Specification<User> specification, Pageable pageable);

    List<User> findAllByAirplaneCompany_Id(String airplaneCompanyId);

    @Query(value =
            "SELECT * FROM APP_PRIVILEGES p WHERE p.ID in (" +
                    "SELECT DISTINCT(pm.PRIVILEGE_ID) FROM GROUPS_PRIVILEGES pm where pm.GROUP_ID in (" +
                    "        SELECT mem.GROUP_ID FROM USERS_GROUPS mem" +
                    "        WHERE mem.USER_ID = :userId))", nativeQuery = true)
    List<Object[]> getPrivilegesByUser(@Param("userId") String userId);
}
