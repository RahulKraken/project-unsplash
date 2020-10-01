package com.kraken.project_unsplash.Models;

import java.io.Serializable;

/**
 * Model class for photo
 */

public class Photo implements Serializable {

  private String id, createdAt, updatedAt, description, alt_description, color;
  private int width, height, likes;
  private Urls urls;
  private PhotoLinks links;
  private User user;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAlt_description() {
    return alt_description;
  }

  public void setAlt_description(String alt_description) {
    this.alt_description = alt_description;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public Urls getUrls() {
    return urls;
  }

  public void setUrls(Urls urls) {
    this.urls = urls;
  }

  public PhotoLinks getLinks() {
    return links;
  }

  public void setLinks(PhotoLinks links) {
    this.links = links;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
