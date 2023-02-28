package com.pega.gpt.launchpadguide.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "prompt_tokens",
        "total_tokens"
})
public class Usage {

    @JsonProperty("prompt_tokens")
    private Integer promptTokens;

    @JsonProperty("total_tokens")
    private Integer totalTokens;

}