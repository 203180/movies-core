package com.example.webmailcore.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Destination extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FROM_COUNTRY_ID")
    private Country fromCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TO_COUNTRY_ID")
    private Country toCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FROM_CITY_ID")
    private City fromCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TO_CITY_ID")
    private City toCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID")
    private DestinationRegion destinationRegion;


}
