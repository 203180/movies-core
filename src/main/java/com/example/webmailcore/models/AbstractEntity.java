package com.example.webmailcore.models;

import com.example.webmailcore.utils.AuthUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final String serialVersionUID = "43272422905946063";

    @Id
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "CREATEDBY", updatable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "MODIFIEDBY")
    private User modifiedBy;

    @Column(name = "DATE_CREATED", updatable = false)
    private Date dateCreated;

    @Column(name = "DATE_MODIFIED")
    private Date dateModified;

    @PrePersist
    private void prePersist() {
        if (id == null) {
            this.setId(UUID.randomUUID().toString());
        }
        if (this.getDateCreated() == null)
            this.setDateCreated(new Date());
        this.setCreatedBy(AuthUtils.getCurrentUser());
    }

    @PreUpdate
    private void preUpdate() {
        this.setDateModified(new Date());
        this.setModifiedBy(AuthUtils.getCurrentUser());
    }

    public void setModifiedBy(User user) {
        if (!(user != null && user.getId() != null && user.getId().equalsIgnoreCase(id))) {
            modifiedBy = user;
        }
    }

    public User getModifiedBy() {
        User user = modifiedBy;
        if (user != null && user.getAirplaneCompany() != null) {
            user.getAirplaneCompany().setCreatedBy(null);
            user.getAirplaneCompany().setModifiedBy(null);
            if (user.getAirplaneCompany().getCity() != null) {
                user.getAirplaneCompany().getCity().setModifiedBy(null);
                user.getAirplaneCompany().getCity().setCreatedBy(null);
            }
            if (user.getAirplaneCompany().getCountry() != null) {
                user.getAirplaneCompany().getCountry().setModifiedBy(null);
                user.getAirplaneCompany().getCountry().setCreatedBy(null);
            }
        }
        return user;
    }

    public void setCreatedBy(User user) {
        if (!(user != null && user.getId() != null && user.getId().equalsIgnoreCase(id))) {
            createdBy = user;
        }
    }

    public User getCreatedBy() {
        User user = createdBy;
        if (user != null && user.getAirplaneCompany() != null) {
            user.getAirplaneCompany().setCreatedBy(null);
            user.getAirplaneCompany().setModifiedBy(null);
            if (user.getAirplaneCompany().getCity() != null) {
                user.getAirplaneCompany().getCity().setModifiedBy(null);
                user.getAirplaneCompany().getCity().setCreatedBy(null);
            }
            if (user.getAirplaneCompany().getCountry() != null) {
                user.getAirplaneCompany().getCountry().setModifiedBy(null);
                user.getAirplaneCompany().getCountry().setCreatedBy(null);
            }
        }
        return user;
    }
}