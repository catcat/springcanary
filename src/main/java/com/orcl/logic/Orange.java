package com.orcl.logic;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class Orange {
    Resource source;

    public Resource getSource() {
        return source;
    }

    public void setSource(Resource source) {
        this.source = source;
    }
}
