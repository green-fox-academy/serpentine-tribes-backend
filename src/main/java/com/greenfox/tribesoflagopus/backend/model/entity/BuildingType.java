package com.greenfox.tribesoflagopus.backend.model.entity;

public enum BuildingType {
  TOWNHALL("townhall"), FARM("farm"), MINE("mine"), BARRACK("barrack");

  private String type;

  private BuildingType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return this.getType();
  }

  public static BuildingType getByName(String type) {
    for (BuildingType buildingType : values()) {
      if(buildingType.getType().equals(type)) {
        return buildingType;
      }
    }
    throw new IllegalArgumentException(type);
  }
}

