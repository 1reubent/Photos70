package app.model;

import java.io.Serializable;

public class TagType implements Serializable {
  private String name;
  private boolean allowsMultipleValues;

  public TagType(String name, boolean allowsMultipleValues) {
    this.name = name.toLowerCase(); // Ensure case-insensitivity
    this.allowsMultipleValues = allowsMultipleValues;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name.toLowerCase(); // Ensure case-insensitivity
  }

  public boolean isMultiValue() {
    return allowsMultipleValues;
  }

  public void setAllowsMultipleValues(boolean allowsMultipleValues) {
    this.allowsMultipleValues = allowsMultipleValues;
  }

  @Override
  public String toString() {
    return name + (allowsMultipleValues ? " (multivalue)" : "");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TagType tagType = (TagType) obj;
    return name.equals(tagType.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}