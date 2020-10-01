package com.kraken.project_unsplash.Models;

import java.io.Serializable;

public class UserLinks implements Serializable {

  private String self, html, photos, likes, portfolio, following, followers;

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public String getPhotos() {
    return photos;
  }

  public void setPhotos(String photos) {
    this.photos = photos;
  }

  public String getLikes() {
    return likes;
  }

  public void setLikes(String likes) {
    this.likes = likes;
  }

  public String getPortfolio() {
    return portfolio;
  }

  public void setPortfolio(String portfolio) {
    this.portfolio = portfolio;
  }

  public String getFollowing() {
    return following;
  }

  public void setFollowing(String following) {
    this.following = following;
  }

  public String getFollowers() {
    return followers;
  }

  public void setFollowers(String followers) {
    this.followers = followers;
  }
}
