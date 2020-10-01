package com.kraken.project_unsplash.Models;

import java.io.Serializable;

public class User implements Serializable {

  private String id, username, name, bio, location, twitter_username, portfolio_url, instagram_username;
  private int total_collections, total_likes, total_photos;
  private ProfilePhoto profile_image;
  private UserLinks links;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getTwitter_username() {
    return twitter_username;
  }

  public void setTwitter_username(String twitter_username) {
    this.twitter_username = twitter_username;
  }

  public String getPortfolio_url() {
    return portfolio_url;
  }

  public void setPortfolio_url(String portfolio_url) {
    this.portfolio_url = portfolio_url;
  }

  public String getInstagram_username() {
    return instagram_username;
  }

  public void setInstagram_username(String instagram_username) {
    this.instagram_username = instagram_username;
  }

  public int getTotal_collections() {
    return total_collections;
  }

  public void setTotal_collections(int total_collections) {
    this.total_collections = total_collections;
  }

  public int getTotal_likes() {
    return total_likes;
  }

  public void setTotal_likes(int total_likes) {
    this.total_likes = total_likes;
  }

  public int getTotal_photos() {
    return total_photos;
  }

  public void setTotal_photos(int total_photos) {
    this.total_photos = total_photos;
  }

  public ProfilePhoto getProfile_image() {
    return profile_image;
  }

  public void setProfile_image(ProfilePhoto profile_image) {
    this.profile_image = profile_image;
  }

  public UserLinks getLinks() {
    return links;
  }

  public void setLinks(UserLinks links) {
    this.links = links;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}