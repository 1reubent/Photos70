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
  //get name
  public String getName() {
    return new File(path).getName();
  }
  public String getPath() { return path; }

  public String getCaption() { return caption; }

  public void setCaption(String caption) { this.caption = caption; }

  public LocalDateTime getDateTaken() { return dateTaken; }

  public Set<Tag> getTags() { return tags; }

  public void addTag(Tag tag) {
    if (!tags.add(tag)) {
      throw new IllegalStateException("Tag already exists.");
    }
  }

  public void removeTag(Tag tag) { tags.remove(tag); }

  //has tag type from name
  public boolean hasTagType(String name) {
    for (Tag tag : tags) {
      if (tag.getName().equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }
  //has tag type from tag type object
  public boolean hasTagType(TagType tagType) {
    for (Tag tag : tags) {
      if (tag.getName().equalsIgnoreCase(tagType.getName())) {
        return true;
      }
    }
    return false;
  }
@Override
public String toString() {
  return String.format("%s (Caption: %s)", getName(), !caption.isEmpty() ? caption : "No caption");
}
}
