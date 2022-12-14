package com.example.webmailcore.models;

import com.example.webmailcore.enums.AirplaneCompanyType;
import com.example.webmailcore.repositories.specifications.AirplaneCompanySpecification;
import com.example.webmailcore.services.AirplaneCompanyService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "AIRPLANE_COMPANY")
@AllArgsConstructor
@NoArgsConstructor
public class AirplaneCompany extends AbstractEntity implements Serializable {

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "PHONE")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID")
    private City city;

    public AirplaneCompany toAirplaneCompanyDTO() {
        AirplaneCompany airplaneCompany = new AirplaneCompany();
        airplaneCompany.setId(this.getId());
        airplaneCompany.setName(this.getName());
        airplaneCompany.setAddress(this.getAddress());
        airplaneCompany.setPostcode(this.getPostcode());
        airplaneCompany.setPhone(this.getPhone());
        airplaneCompany.setCountry(this.getCountry());
        return airplaneCompany;
    }

    @Column(name = "AIRPLANE_COMPANY_TYPE")
    private AirplaneCompanyType type;

}
