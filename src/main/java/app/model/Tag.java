package app.model;

import java.io.Serializable;
import java.util.*;

public class Tag implements Serializable {
  private String name;
  private Set<String> values;

  public Tag(String name, String value) {
    this.name = name.toLowerCase();
    this.values = new HashSet<>();
    this.values.add(value.toLowerCase());
  }

  public String getName() {
    return name;
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
    return name.equals(other.name) && values.equals(other.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, values);
  }

  @Override
  public String toString() {
    return name + "=" + String.join(", ", values);
  }
}