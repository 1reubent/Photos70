package app.model;

import java.io.Serializable;
import java.util.Objects;

public class Tag implements Serializable {
  private String name;
  private String value;

  public Tag(String type, String value) {
    this.name = type.toLowerCase();
    this.value = value.toLowerCase();
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Tag)) return false;
    Tag other = (Tag) obj;
    return name.equals(other.name) && value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public String toString() {
    return name + "=" + value;
  }
}
