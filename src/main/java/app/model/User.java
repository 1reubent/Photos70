package app.model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
  private String username;
  private Map<String, Album> albums;

  public User(String username) {
    this.username = username;
    this.albums = new HashMap<>();
  }

  public String getUsername() { return username; }

  public Map<String, Album> getAlbums() {
    return albums;
  }

  public Album getAlbum(String name) {
    return albums.get(name);
  }

  public boolean addAlbum(String name) {
    if (albums.containsKey(name)) return false;
    albums.put(name, new Album(name));
    System.out.println("Album added: " + name);
    System.out.println("Current albums: " + albums.keySet());
    return true;
  }
  public void addPhotoToAlbum(String albumName, Photo photo) {
    albums.get(albumName).addPhoto(photo);
  }
  public void removePhotoFromAlbum(String albumName, Photo photo) {
    albums.get(albumName).removePhoto(photo);
  }
  public boolean deleteAlbum(String name) {
    return albums.remove(name) != null;
  }

  public boolean renameAlbum(String oldName, String newName) {
    if (!albums.containsKey(oldName) || albums.containsKey(newName)) return false;
    Album album = albums.remove(oldName);
    album.setName(newName);
    albums.put(newName, album);
    return true;
  }
}

