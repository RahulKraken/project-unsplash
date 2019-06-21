package com.kraken.project_unsplash.Models;

import java.io.Serializable;

public class Tag implements Serializable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
