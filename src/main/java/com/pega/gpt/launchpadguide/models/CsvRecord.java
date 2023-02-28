package com.pega.gpt.launchpadguide.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvRecord {

    private String name;
    private String description;

    public String getConcatenatedString() {
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(description)) {
            return this.name.trim() + " , " + this.description.trim();
        }
        return null;
    }

}
