package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

  @Autowired
  KingdomRepository kingdomRepository;

  @Autowired
  DtoService dtoService;

  public BuildingListDto createBuildingList(long userId) {
    List<Building> buildingsToConvertToDto = kingdomRepository.findOneByUserId(userId).getBuildings();
    List<BuildingDto> buildingDtos = dtoService.convertFromBuildings(buildingsToConvertToDto);
    BuildingListDto buildingsToReturn = BuildingListDto.builder()
        .buildings(buildingDtos)
        .build();
    return buildingsToReturn;
  }

}
