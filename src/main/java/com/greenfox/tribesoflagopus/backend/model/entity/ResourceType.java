package com.greenfox.tribesoflagopus.backend.model.entity;

public enum ResourceType {
  GOLD("gold"), FOOD("food");

  private String resource;

  private ResourceType(String resource) {
    this.resource = resource;
  }

  public String getResource() {
    return resource;
  }

  @Override
  public String toString() {
    return this.getResource();
  }

  public static ResourceType getByName(String type) {
    for (ResourceType resourceType : values()) {
      if(resourceType.getResource().equals(type)) {
        return resourceType;
      }
    }
    throw new IllegalArgumentException(type);
  }
}
