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

  public User addUser(String username) {
    if (users.containsKey(username)) return null;
    User new_user = new User(username);
    users.put(username, new_user);
    return new_user;
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