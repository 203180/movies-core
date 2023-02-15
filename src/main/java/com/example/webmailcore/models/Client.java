package com.example.webmailcore.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "CLIENT")
@AllArgsConstructor
@NoArgsConstructor
public class Client extends AbstractEntity implements Serializable {

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "AGE")
    private String age;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    public String getDisplayName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
