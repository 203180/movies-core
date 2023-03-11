package com.example.webmailcore.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;

@Getter
@Setter
public class DestinationPairDTO {

    @JsonProperty
    public String cityFrom;

    @JsonProperty
    public String cityTo;

    @JsonProperty
    public String countryFrom;

    @JoinColumn
    public String countryTo;

    public DestinationPairDTO(String cityFrom, String cityTo, String countryFrom, String countryTo) {
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.countryFrom = countryFrom;
        this.countryTo = countryTo;
    }
}
