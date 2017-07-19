package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

  public Timestamp calculateBuildingTime(Timestamp startedAt,
      BuildingType buildingType,
      int level) {
    Timestamp finishedAt;
    if (buildingType.equals(BuildingType.TOWNHALL) && level == 1) {
      finishedAt = startedAt;
      return finishedAt;
    }
    return calculateFinishedAtTime(startedAt);
  }

  public Timestamp calculateFinishedAtTime (Timestamp startedAt){
    Timestamp finishedAt;
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    long startedAsMillis = startedAt.getTime();
    long minuteToDelayWith = 1;
    long delayInMillis = minuteToDelayWith*60*1000;
    if (currentTime.getTime() - startedAt.getTime() < delayInMillis){
      finishedAt = new Timestamp(0);
    } else {
      finishedAt = new Timestamp(startedAsMillis+delayInMillis);
    }
    return finishedAt;
  }

  public Building setBuildingFinishedTime(Building building) {
    Timestamp finishedAt =
        calculateBuildingTime(building.getStartedAt(), building.getType(),building.getLevel());
    building.setFinishedAt(finishedAt);
    return building;
  }

  public List<Building> setBuildingListFinishedTimes(List<Building> buildings) {
    for (Building building : buildings) {
      building = setBuildingFinishedTime(building);
    }
    return buildings;
  }
}
