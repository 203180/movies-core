package com.example.webmailcore.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CITY")
@AllArgsConstructor
@NoArgsConstructor
public class City extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "POSTCODE")
    private String postcode;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;
}
