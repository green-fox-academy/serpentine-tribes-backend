package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import java.sql.Timestamp;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

  public Timestamp calculateBuildingTime(Timestamp startedAt, BuildingType buildingType) {
    Timestamp finishedAt;
    if (buildingType.equals(BuildingType.TOWNHALL)) {
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

}
