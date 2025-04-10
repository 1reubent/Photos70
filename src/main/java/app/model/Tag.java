package app.model;

import java.io.Serializable;
import java.util.*;

public class Tag implements Serializable {
  private TagType type;
  private Set<String> values;

  public Tag(TagType type, String value) {
    this.type = type;
    this.values = new HashSet<>();
    this.values.add(value.toLowerCase());
  }

  public String getName() {
    return type.getName();
  }

  public Set<String> getValues() {
    return values;
  }

  public void addValue(String value, boolean allowsMultipleValues) {
    if (!allowsMultipleValues && !values.isEmpty()) {
      throw new IllegalStateException("This tag type allows only one value.");
    }
    values.add(value.toLowerCase());
  }

  public void removeValue(String value) {
    values.remove(value.toLowerCase());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Tag)) return false;
    Tag other = (Tag) obj;
    return type.equals(other.type) && values.equals(other.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, values);
  }

  @Override
  public String toString() {
    return type.getName() + "=" + String.join(", ", values);
  }
}