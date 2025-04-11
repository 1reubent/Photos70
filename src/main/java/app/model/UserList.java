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

  //add user from name
  public User addUser(String username) {
    if (hasUser(username)) throw new IllegalArgumentException("User already exists: " + username);
    User new_user = new User(username);
    users.put(username, new_user);
    return new_user;
  }

  //add user from user object
  public User addUser(User user) {
    if (hasUser(user.getUsername())) return null;
    users.put(user.getUsername(), user);
    return user;
  }

  public boolean deleteUser(String username) {
    return users.remove(username) != null;
  }

  public User getUser(String username) {
    return users.get(username);
  }

  public Collection<User> getAllUsers() {
    return users.values();
  }

  public Set<String> getAllUsernames() {
    return users.keySet();
  }


  public boolean hasUser(String username) {
    return users.containsKey(username);
  }
}