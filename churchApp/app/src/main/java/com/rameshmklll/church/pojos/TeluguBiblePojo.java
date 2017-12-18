package com.rameshmklll.church.pojos;

/**
 * Created by MRamesh on 18-12-2017.
 */

public class TeluguBiblePojo {

    String name;
    String version;
    int id_;
    int image;

    public TeluguBiblePojo(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }


}