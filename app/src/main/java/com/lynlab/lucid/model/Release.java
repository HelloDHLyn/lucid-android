package com.lynlab.lucid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lyn
 * @since 2016/09/03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Release {

    @JsonProperty("application_id")
    private int applicationId;
    @JsonProperty("is_latest")
    private boolean isLatest;
    @JsonProperty("is_release")
    private boolean isRelease;
    @JsonProperty("version_code")
    private int versionCode;
    @JsonProperty("version_name")
    private String verisonName;
    @JsonProperty("path")
    private String path;

    public int getApplicationId() {
        return applicationId;
    }

    public boolean isLatest() {
        return isLatest;
    }

    public boolean isRelease() {
        return isRelease;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVerisonName() {
        return verisonName;
    }

    public String getPath() {
        return path;
    }
}
