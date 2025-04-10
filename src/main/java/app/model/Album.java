package app.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Album implements Serializable {
  private static boolean isInitializingStock = false;
  private String name;
  private List<Photo> photos;

  public Album(String name) {
    this.name = name;
    this.photos = new ArrayList<>();
    System.out.println("Album created: " + name);
  }
  public static void setInitializingStock(boolean initializing) {
    isInitializingStock = initializing;
  }

  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public List<Photo> getPhotos() { return photos; }

  //get photo by path
  public Photo getPhoto(String path) {
    for (Photo photo : photos) {
      if (photo.getPath().equals(path)) {
        return photo;
      }
    }
    return null;
  }

  public void addPhoto(Photo photo) {
    if (name.equalsIgnoreCase("stock") && !isInitializingStock) {
      throw new IllegalStateException("Cannot add photos to stock album");
    }
    if (photos.contains(photo)) {
      throw new IllegalArgumentException("Photo already exists in the album!");
    }
    photos.add(photo);
  }

  public void removePhoto(Photo photo) {
    if (name.equalsIgnoreCase("stock") && !isInitializingStock) {
      throw new IllegalStateException("Cannot remove stock photos from stock album");
    }
    photos.remove(photo);
  }
  public int getPhotoCount() {
    return photos.size();
  }
  public String getDateRange() {
    if (photos.isEmpty()) {
      return "No photos";
    }
    return getEarliestDate().toString() + " -- " + getLatestDate().toString();
  }
  public LocalDateTime getEarliestDate() {
    return photos.stream().map(Photo::getDateTaken).min(LocalDateTime::compareTo).orElse(null);
  }

  public LocalDateTime getLatestDate() {
    return photos.stream().map(Photo::getDateTaken).max(LocalDateTime::compareTo).orElse(null);
  }
  //to string
  @Override
  public String toString() {
    return String.format("Name: %s | %d photos | Date Range: %s", name, getPhotoCount(), getDateRange());
  }

  //equals and hashcode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Album)) return false;
    Album album = (Album) o;
    return Objects.equals(name, album.name);
  }
  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

}
