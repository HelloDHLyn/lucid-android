package com.lynlab.lucid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lyn
 * @since 2016/09/03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("package")
    private String packageName;
    @JsonProperty("platform")
    private String platform;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }
}
