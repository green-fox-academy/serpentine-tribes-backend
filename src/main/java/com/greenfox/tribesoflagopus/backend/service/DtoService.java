package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomDto;
import com.greenfox.tribesoflagopus.backend.model.dto.LocationDto;
import com.greenfox.tribesoflagopus.backend.model.dto.ResourceDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopListDto;
import com.greenfox.tribesoflagopus.backend.model.dto.UserTokenDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DtoService {

  public UserTokenDto createUserTokenDto(String token) {
    return UserTokenDto.builder()
        .status("ok")
        .token(token)
        .build();
  }

  public List<BuildingDto> convertFromBuildings(List<Building> buildings) {
    List<BuildingDto> listOfBuildingDtos = new ArrayList<>();

    for (Building building : buildings) {
      BuildingDto buildingDto = BuildingDto.builder()
          .id(building.getId())
          .type(building.getType())
          .level(building.getLevel())
          .hp(building.getHp())
          .build();
      listOfBuildingDtos.add(buildingDto);
    }
    return listOfBuildingDtos;
  }

  public List<ResourceDto> convertFromResources(List<Resource> resources) {
    List<ResourceDto> listOfResourceDtos = new ArrayList<>();

    for (Resource resource : resources) {
      ResourceDto resourceDto = ResourceDto.builder()
          .type(resource.getType())
          .amount(resource.getAmount())
          .generation(resource.getGeneration())
          .build();
      listOfResourceDtos.add(resourceDto);
    }
    return listOfResourceDtos;
  }

  public List<TroopDto> convertFromTroops(List<Troop> troops) {
    List<TroopDto> listOfTroopDtos = new ArrayList<>();

    for (Troop troop : troops) {
      TroopDto troopDto = convertFromTroop(troop);
      listOfTroopDtos.add(troopDto);
    }
    return listOfTroopDtos;
  }

  public TroopDto convertFromTroop(Troop troop) {
    return TroopDto.builder()
        .id(troop.getId())
        .level(troop.getLevel())
        .hp(troop.getHp())
        .attack(troop.getAttack())
        .defence(troop.getDefence())
        .build();
  }

  public LocationDto convertFromLocation(Location location) {
    if (location == null) {
      return null;
    }
    return LocationDto.builder()
        .x(location.getX())
        .y(location.getY())
        .build();
  }

  public KingdomDto convertFromKingdom(Kingdom kingdom) {
    return KingdomDto.builder()
        .id(kingdom.getId())
        .name(kingdom.getName())
        .userId(kingdom.getUser().getId())
        .buildings(convertFromBuildings(kingdom.getBuildings()))
        .resources(convertFromResources(kingdom.getResources()))
        .troops(convertFromTroops(kingdom.getTroops()))
        .location(convertFromLocation(kingdom.getLocation()))
        .build();
  }

  public BuildingDto convertfromBuilding(Building building) {
    return BuildingDto.builder()
        .id(building.getId())
        .type(building.getType())
        .level(building.getLevel())
        .hp(building.getHp())
        .build();
  }

  public TroopListDto createTroopListDto(List<Troop> troops) {
    List<TroopDto> listOfTroopDtos = convertFromTroops(troops);
    return TroopListDto.builder()
        .troops(listOfTroopDtos)
        .build();
  }
}
