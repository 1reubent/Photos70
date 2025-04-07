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
    System.out.println("Album created: " + name);
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
  public int getPhotoCount() {
    return photos.size();
  }
  public String getDateRange() {
    if (photos.isEmpty()) {
      return "No photos";
    }
    // Assuming Photo class has a getDate() method returning a Date object
    LocalDateTime earliest = photos.get(0).getDateTaken();
    LocalDateTime latest = photos.get(0).getDateTaken();
    for (Photo photo : photos) {
      if (photo.getDateTaken().isBefore(earliest)) {
        earliest = photo.getDateTaken();
      }

      if (photo.getDateTaken().isAfter(latest)) {
        latest = photo.getDateTaken();
      }
    }
    return earliest.toString() + " - " + latest.toString();
  }
  public LocalDateTime getEarliestDate() {
    return photos.stream().map(Photo::getDateTaken).min(LocalDateTime::compareTo).orElse(null);
  }

  public LocalDateTime getLatestDate() {
    return photos.stream().map(Photo::getDateTaken).max(LocalDateTime::compareTo).orElse(null);
  }
}
