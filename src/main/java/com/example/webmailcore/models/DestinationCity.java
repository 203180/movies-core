package com.example.webmailcore.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "DESTINATION_CITY")
@AllArgsConstructor
@NoArgsConstructor
public class DestinationCity extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    @Column(name = "LONGITUDE")
    private Float longitude;

    @Column(name = "LATITUDE")
    private Float latitude;
}
