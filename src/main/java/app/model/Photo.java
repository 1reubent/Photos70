package app.model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Photo implements Serializable {
  private String path;
  private String caption;
  private LocalDateTime dateTaken;
  private Set<Tag> tags;

  public Photo(String filePath) {
    this.path = filePath;
    this.caption = "";
    this.tags = new HashSet<>();
    File file = new File(filePath);
    this.dateTaken = LocalDateTime.ofInstant(
            new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()
    );
  }

  public String getPath() { return path; }

  public String getCaption() { return caption; }

  public void setCaption(String caption) { this.caption = caption; }

  public LocalDateTime getDateTaken() { return dateTaken; }

  public Set<Tag> getTags() { return tags; }

  public void addTag(Tag tag) { tags.add(tag); }

  public void removeTag(Tag tag) { tags.remove(tag); }

  @Override
  public String toString() {
    return caption + " - " + dateTaken.toString();
  }
}
