package com.greenfox.tribesoflagopus.backend.model.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kingdom {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String name;
  private Long userId;

  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Resource> resources;

  public void addResources(Resource resource) {
    resources.add(resource);
    resource.setKingdom(this);
  }

  public void removeResources(Resource resource) {
    resource.setKingdom(null);
    this.resources.remove(resource);
  }
}
