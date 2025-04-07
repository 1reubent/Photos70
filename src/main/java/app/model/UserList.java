package app.model;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;
import java.util.*;

public class UserList implements Serializable {
  private static final long serialVersionUID = 1L;
  private Map<String, User> users;

  public UserList() {
    users = new HashMap<>();
  }
  public void addAlbum(String username, String albumName) {
    User user = users.get(username);
    if (user != null) {
      user.addAlbum(albumName);
    }
  }
  public void addUserPhoto(String username, String albumName, String photoPath) {
    User user = users.get(username);
    if (user != null) {
      user.addPhotoToAlbum(albumName, new Photo(photoPath));
    }
  }
  public boolean addUser(String username) {
    if (users.containsKey(username)) return false;
    users.put(username, new User(username));
    return true;
  }

  public boolean deleteUser(String username) {
    return users.remove(username) != null;
  }

  public User getUser(String username) {
    return users.get(username);
  }

  public Map<String, User> getAllUsers() {
    return users;
  }

  public boolean hasUser(String username) {
    return users.containsKey(username);
  }
}