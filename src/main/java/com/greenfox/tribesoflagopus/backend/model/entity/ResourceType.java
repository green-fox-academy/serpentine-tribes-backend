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
}
