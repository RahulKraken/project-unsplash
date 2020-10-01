package com.kraken.project_unsplash.Models;

import java.io.Serializable;

public class CollectionLinks implements Serializable {
  private String self, html, photos, related;

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

  public String getRelated() {
    return related;
  }

  public void setRelated(String related) {
    this.related = related;
  }
}
