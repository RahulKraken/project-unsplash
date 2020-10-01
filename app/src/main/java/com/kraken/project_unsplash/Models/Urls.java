package com.kraken.project_unsplash.Models;

import java.io.Serializable;

public class Urls implements Serializable {

  private String raw, full, regular, small, thumb;

  public String getRaw() {
    return raw;
  }

  public void setRaw(String raw) {
    this.raw = raw;
  }

  public String getFull() {
    return full;
  }

  public void setFull(String full) {
    this.full = full;
  }

  public String getRegular() {
    return regular;
  }

  public void setRegular(String regular) {
    this.regular = regular;
  }

  public String getSmall() {
    return small;
  }

  public void setSmall(String small) {
    this.small = small;
  }

  public String getThumb() {
    return thumb;
  }

  public void setThumb(String thumb) {
    this.thumb = thumb;
  }
}
