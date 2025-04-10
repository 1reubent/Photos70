package app.model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
  private String username;
  private Map<String, Album> albums; // Album Name -> Album
  private Map<String, Boolean> myTagTypes; // User-specific tag types; Tag Name -> Allows Multiple Values

  public User(String username) {
    this.username = username;
    this.albums = new HashMap<>();
    this.myTagTypes = new HashMap<>();
    //initialize default tag types
    myTagTypes.put("location", false);
    myTagTypes.put("people", true);
  }

  public String getUsername() {
    return username;
  }


  public Collection<Album> getAlbums() {
    return albums.values();
  }

  public Set<String> getAlbumNames() {
    return albums.keySet();
  }
  public Album getAlbum(String name) {
    return albums.get(name);
  }

  public Map<String, Boolean> getTagTypes() {
    return myTagTypes;
  }
  public Set<String> getTagTypeNames() {
    return myTagTypes.keySet();
  }

  public boolean isTagTypeDefined(String name) {
    return myTagTypes.containsKey(name.toLowerCase());
  }

  public boolean addTagType(String name, boolean allowsMultipleValues) {
    if (myTagTypes.containsKey(name.toLowerCase())) return false;
    myTagTypes.put(name.toLowerCase(), allowsMultipleValues);
    return true;
  }

  public boolean removeTagType(String name) {
    return myTagTypes.remove(name.toLowerCase()) != null;
  }

  public boolean tagTypeAllowsMultipleValues(String name) {
    return myTagTypes.getOrDefault(name.toLowerCase(), false);
  }

  public Album addAlbum(String name) {
    if (albums.containsKey(name)) return null;
    Album new_album = new Album(name);
    albums.put(name, new_album);
    return new_album;
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