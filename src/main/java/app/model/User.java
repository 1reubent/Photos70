package app.model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
  private String username;
  private Map<String, Album> albums; // Album Name -> Album
  private List<TagType> myTagTypes; // User-specific tag types

  public User(String username) {
    this.username = username;
    this.albums = new HashMap<>();
    this.myTagTypes = new ArrayList<>();
    // Initialize default tag types
  addTagType("location", false);
  addTagType("people", true);
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

  public List<TagType> getTagTypes() {
    return myTagTypes;
  }

  public TagType getTagType(String name) {
    return myTagTypes.stream()
            .filter(tagType -> tagType.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
  }

  //add tag type from name and isMultiValue
  public boolean addTagType(String name, boolean isMultiValue) {
    if (isTagTypeDefined(name)) return false;
    TagType newTagType = new TagType(name, isMultiValue);
    myTagTypes.add(newTagType);
    return true;
  }

  //add tag type from tagType object
  public boolean addTagType(TagType tagType) {
    if (!isTagTypeDefined(tagType)) return false;
    myTagTypes.add(tagType);
    return true;
  }


  public boolean removeTagType(String name) {
    TagType tagType = getTagType(name);
    if (tagType == null) return false;
    return myTagTypes.remove(tagType);
  }

  //is tag type defined from name
  public boolean isTagTypeDefined(String name) {
    return getTagType(name) != null;
  }

  //is tag type defined from tag type object
  public boolean isTagTypeDefined(TagType tagType) {
    return getTagType(tagType.getName()) != null;
  }


//  public boolean isMultiValueTagType(String name) {
//    TagType tagType = getTagType(name);
//    return tagType != null && tagType.isMultiValue();
//  }

  public Album addAlbum(String name) {
    if (albums.containsKey(name)) return null;
    Album newAlbum = new Album(name);
    albums.put(name, newAlbum);
    return newAlbum;
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

  //toString
  @Override
  public String toString() {
    return "User{" +
            "username='" + username + '\'' +
            ", albums=" + albums +
            ", myTagTypes=" + myTagTypes +
            '}';
  }
}