package com.pega.gpt.launchpadguide.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimilarityRecord {

    private String text;
    private String similarities;

}
