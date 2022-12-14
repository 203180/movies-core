package com.example.webmailcore.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "DESTINATION_REGION")
@AllArgsConstructor
@NoArgsConstructor
public class DestinationRegion extends AbstractEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

}
