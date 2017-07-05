package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import com.greenfox.tribesoflagopus.backend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  public static final Integer LOCATION_MIN_VALUE = 1;
  public static final Integer LOCATION_MAX_VALUE = 100;

  private final LocationRepository locationRepository;

  @Autowired
  public LocationService(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
  }

  public Location createNewValidLocation() {
    Location location = new Location();
    do {
      location.setX(generateRandomNumber(
          LOCATION_MIN_VALUE, LOCATION_MAX_VALUE));
      location.setY(generateRandomNumber(
          LOCATION_MIN_VALUE, LOCATION_MAX_VALUE));
    } while (locationRepository.existsByXAndY(location.getX(), location.getY()));
    return location;
  }

  private Integer generateRandomNumber(int min, int max) {
    int random = min + (int) (Math.random() * (max + 1));
    Integer randomNumber = Integer.valueOf(random);
    return randomNumber;
  }
}
