package com.example.webmailcore.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final String serialVersionUID = "43272422905946063";

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "DATE_CREATED", updatable = false)
    private Date dateCreated;

    @Column(name = "DATE_MODIFIED")
    private Date dateModified;

}