package com.example.webmailcore.models.settings;

import com.example.webmailcore.models.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SYSTEM_SETTINGS_PROPS")
public class SystemSettingsProp extends AbstractEntity implements Serializable {

    @Column(name = "PROP_KEY")
    private String key;

    @Column(name = "PROP_VAL")
    private String value;
}


