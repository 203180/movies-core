package com.example.webmailcore.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "LOYALTY_CARD")
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyCard extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "KILOMETERS")
    private String kilometers;

    @Column(name = "MIN_EXPENDITURE")
    private String minExpenditure;

    @Column(name = "DISCOUNT")
    private String discount;


}
