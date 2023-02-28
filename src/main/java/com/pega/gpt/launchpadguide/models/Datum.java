package com.pega.gpt.launchpadguide.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "object",
        "index",
        "embedding"
})
@Data
public class Datum {

    @JsonProperty("object")
    private String object;

    @JsonProperty("index")
    private Integer index;

    @JsonProperty("embedding")
    private List<Double> embedding;

}
