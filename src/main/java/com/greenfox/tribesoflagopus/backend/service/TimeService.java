package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
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
    long t = startedAt.getTime();
    long minuteToDelayWith = 1;
    long m = minuteToDelayWith*60*1000;
    if (currentTime.getTime() - startedAt.getTime() < m){
      finishedAt = new Timestamp(0);
    } else {
      finishedAt = new Timestamp(t+m);
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

  public List<Kingdom> setKingdomFinishedTimes(List<Kingdom> kingdoms) {
    for (Kingdom kingdom : kingdoms) {
      setBuildingListFinishedTimes(kingdom.getBuildings());
      setTroopListFinishedTimes(kingdom.getTroops());
    }
    return kingdoms;
  }

  public Troop setTroopFinishedTime(Troop troop) {
    Timestamp finishedAt = calculateFinishedAtTime(troop.getStartedAt());
    troop.setFinishedAt(finishedAt);
    return troop;
  }

  public List<Troop> setTroopListFinishedTimes(List<Troop> troops) {
    for (Troop troop : troops) {
      troop = setTroopFinishedTime(troop);
    }
    return troops;
  }
}
