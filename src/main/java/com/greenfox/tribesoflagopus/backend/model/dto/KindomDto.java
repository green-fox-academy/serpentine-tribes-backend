package com.greenfox.tribesoflagopus.backend.model.dto;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import java.util.ArrayList;
import java.util.List;

public class KindomDto implements JsonDto {

  private final List<Troop> troops = new ArrayList<>();

  private final List<Building> buildings = new ArrayList<>();

  private final List<Resource> resources = new ArrayList<>();

  private long id;

  private String name;

  private Long userId;

  private Integer[] location = new Integer[2];

}
