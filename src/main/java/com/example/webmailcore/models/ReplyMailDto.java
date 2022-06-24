package com.example.webmailcore.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ReplyMailDto {

    @JsonProperty
    String replyToID;

    @JsonProperty
    String content;

    @JsonProperty
    ArrayList<String> ticketIds;
}