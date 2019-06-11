package com.kraken.project_unsplash.Utils.Models;

import java.net.URL;

/**
 * Model class for photo
 */

public class PhotoModel {

    private String ID, description, userName, location;
    private URL raw, full, regular;
    private int height, width, likes;

    // getter and setter methods

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public URL getRaw() {
        return raw;
    }

    public void setRaw(URL raw) {
        this.raw = raw;
    }

    public URL getFull() {
        return full;
    }

    public void setFull(URL full) {
        this.full = full;
    }

    public URL getRegular() {
        return regular;
    }

    public void setRegular(URL regular) {
        this.regular = regular;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
