package com.example.webmailcore.models;

import com.example.webmailcore.enums.AncillaryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User  extends AbstractEntity implements Serializable {

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "IS_ENABLED")
    private Boolean isEnabled;

    @Column(name ="AGE")
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "AIRPLANE_COMPANY_ID")
    private AirplaneCompany airplaneCompany;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "USERS_GROUPS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID")
    )
    private List<Group> groups = new ArrayList<>();

    @Transient
    private List<Privilege> privileges = new ArrayList<>();

    @Transient
    @JsonProperty
    private String newPassword;

    public String getDisplayName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    @ManyToOne
    @JoinColumn(name = "LOYALTY_CARD_ID")
    private LoyaltyCard loyaltyCard;

    @Transient
    private Integer totalPrice;

    @Transient
    private Integer ticketCount;

    @Column(name = "COUNTRY")
    private String country;
}
