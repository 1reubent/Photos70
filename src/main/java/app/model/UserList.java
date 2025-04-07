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
  public void initializeStockPhotos() {
    User stockUser = new User("stock");
    stockUser.addAlbum("stock");

    String[] stockPhotoPaths = {
            "src/main/resources/stock1.jpg",
            "src/main/resources/stock2.jpg",
            "src/main/resources/stock3.jpg",
            "src/main/resources/stock4.jpg",
            "src/main/resources/stock5.jpg"
    };

    for (String path : stockPhotoPaths) {
      stockUser.addPhotoToAlbum("stock", new Photo(path));
    }

    users.put("stock", stockUser);
    try {
      save(this, getClass().getResource("/users.json").getPath());
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Stock user and album initialized: " + stockUser.getAlbums().keySet());

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

  public static void save(UserList list, String filename) throws IOException {
    JSONObject json = new JSONObject();
    for (User user : list.getAllUsers().values()) {
      json.put(user.getUsername(), new JSONObject(user));
    }
    try (FileWriter file = new FileWriter(filename)) {
      file.write(json.toString(4));
    }
  }

  public static UserList load(String filename) throws IOException {
    UserList userList = new UserList();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      StringBuilder jsonText = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        jsonText.append(line);
      }
      JSONObject json = new JSONObject(jsonText.toString());
      for (String key : json.keySet()) {
        JSONObject userJson = json.getJSONObject(key);
        User user = new User(userJson.getString("username"));
        userList.users.put(user.getUsername(), user);
      }
    }
    return userList;
  }
}