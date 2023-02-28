package com.pega.gpt.launchpadguide.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "object",
        "data",
        "model",
        "usage"
})
@Data
public class Result {

    @JsonProperty("object")
    private String object;
    @JsonProperty("data")
    private List<Datum> data;
    @JsonProperty("model")
    private String model;
    @JsonProperty("usage")
    private Usage usage;

}