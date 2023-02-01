package com.example.webmailcore.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "COUNTRY")
@AllArgsConstructor
@NoArgsConstructor
public class Country extends AbstractEntity implements Serializable {

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_EN")
    private String nameEn;

    @Column(name = "CODE")
    private String code;

    @Column(name = "CAPITAL")
    private String capital;

}
