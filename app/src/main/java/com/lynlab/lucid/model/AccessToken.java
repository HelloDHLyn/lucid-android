package com.lynlab.lucid.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lyn
 * @since 2016/09/07
 */
public class AccessToken {

    @JsonProperty("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
