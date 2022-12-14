package com.example.webmailcore.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "GROUPS")
public class Group extends AbstractEntity implements Serializable {

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "AIRPLANE_COMPANY_ID")
    private AirplaneCompany airplaneCompany;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USERS_GROUPS",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    List<User> users;

    @Transient
    @JsonProperty
    List<User> members;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "GROUPS_PRIVILEGES",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID")
    )
    List<Privilege> privileges;

}
