package com.example.webmailcore.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "APP_PRIVILEGES")
public class Privilege extends AbstractEntity implements Serializable {

    @Column(name = "NAME")
    private String name;

}

