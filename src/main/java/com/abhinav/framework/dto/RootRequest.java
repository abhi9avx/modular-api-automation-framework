package com.abhinav.framework.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RootRequest {
  private User user;
  private Project project;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class User {
    private int id;
    private String name;
    private List<String> roles;
    private Profile profile;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Profile {
    private String email;
    private List<Phone> phones;
    private Address address;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Phone {
    private String type;
    private String number;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Address {
    private String street;
    private String city;
    private Geo geo;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Geo {
    private double lat;
    private double lng;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Project {
    private String name;
    private List<Module> modules;
    private boolean active;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Module {
    private String module;
    private String status;
  }
}
