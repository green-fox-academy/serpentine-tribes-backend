package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class BuildingType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String buildingType;

  public BuildingType(){
  }

  public BuildingType(String buildingType){
    this.buildingType = buildingType;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getBuildingType() {
    return buildingType;
  }

  public void setBuildingType(String buildingType) {
    this.buildingType = buildingType;
  }
}
