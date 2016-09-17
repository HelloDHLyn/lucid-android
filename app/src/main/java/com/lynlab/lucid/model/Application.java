package com.lynlab.lucid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author lyn
 * @since 2016/09/03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class Application {

    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;
    @JsonProperty("package")
    String packageName;
    @JsonProperty("platform")
    String platform;

    public Application() {

    }

    @ParcelConstructor
    public Application(int id, String name, String packageName, String platform) {
        this.id = id;
        this.name = name;
        this.packageName = packageName;
        this.platform = platform;
    }

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
