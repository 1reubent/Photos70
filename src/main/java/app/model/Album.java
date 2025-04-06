package app.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Album implements Serializable {
  private String name;
  private List<Photo> photos;

  public Album(String name) {
    this.name = name;
    this.photos = new ArrayList<>();
  }

  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public List<Photo> getPhotos() { return photos; }

  public void addPhoto(Photo photo) {
    if (!photos.contains(photo)) {
      photos.add(photo);
    }
  }

  public void removePhoto(Photo photo) {
    photos.remove(photo);
  }

  public LocalDateTime getEarliestDate() {
    return photos.stream().map(Photo::getDateTaken).min(LocalDateTime::compareTo).orElse(null);
  }

  public LocalDateTime getLatestDate() {
    return photos.stream().map(Photo::getDateTaken).max(LocalDateTime::compareTo).orElse(null);
  }
}
