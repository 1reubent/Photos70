package app.model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
  private String username;
  //TODO: why is this a map? album class already has a name field
  private Map<String, Album> albums;
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

  public boolean addTagType(String name, boolean allowsMultipleValues) {
    if (myTagTypes.containsKey(name.toLowerCase())) return false;
    myTagTypes.put(name.toLowerCase(), allowsMultipleValues);
    return true;
  }

  public boolean removeTagType(String name) {
    return myTagTypes.remove(name.toLowerCase()) != null;
  }

  public boolean allowsMultipleValues(String name) {
    return myTagTypes.getOrDefault(name.toLowerCase(), false);
  }

  public boolean hasTagType(String name) {
    return myTagTypes.containsKey(name.toLowerCase());
  }

  public boolean addAlbum(String name) {
    if (albums.containsKey(name)) return false;
    albums.put(name, new Album(name));
    return true;
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