package app.model;

import java.io.Serializable;
import java.util.Objects;

public class Tag implements Serializable {
  private String type;
  private String value;

  public Tag(String type, String value) {
    this.type = type.toLowerCase();
    this.value = value.toLowerCase();
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Tag)) return false;
    Tag other = (Tag) obj;
    return type.equals(other.type) && value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value);
  }

  @Override
  public String toString() {
    return type + "=" + value;
  }
}
