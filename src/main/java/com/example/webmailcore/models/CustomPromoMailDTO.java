package com.example.webmailcore.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomPromoMailDTO {

    @JsonProperty
    public List<User> users;

    @JsonProperty
    public String mailSubject;

    @JsonProperty
    public String mailContent;


}
