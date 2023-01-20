package com.example.webmailcore.models.idm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ForgotPasswordToken {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("validUntil")
    private Date validUntil;
}
