package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceXLaunchDto {

  private Fairings fairings;
  private Links links;
  private String static_fire_date_utc;
  private Long static_fire_date_unix;
  private Boolean net;
  private Integer window;
  private String rocket;
  private Boolean success;
  private List<Failure> failures;
  private String details;
  private List<Object> crew;
  private List<Object> ships;
  private List<Object> capsules;
  private List<Object> payloads;
  private String launchpad;
  private Integer flight_number;
  private String name;
  private String date_utc;
  private Long date_unix;
  private String date_local;
  private String date_precision;
  private Boolean upcoming;
  private List<Core> cores;
  private Boolean auto_update;
  private Boolean tbd;
  private String launch_library_id;
  private String id;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Fairings {
    private Boolean reused;
    private Boolean recovery_attempt;
    private Boolean recovered;
    private List<String> ships;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Links {
    private Patch patch;
    private Reddit reddit;
    private Flickr flickr;
    private String presskit;
    private String webcast;
    private String youtube_id;
    private String article;
    private String wikipedia;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Patch {
    private String small;
    private String large;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Reddit {
    private String campaign;
    private String launch;
    private String media;
    private String recovery;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Flickr {
    private List<String> small;
    private List<String> original;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Failure {
    private Integer time;
    private Integer altitude;
    private String reason;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Core {
    private String core;
    private Integer flight;
    private Boolean gridfins;
    private Boolean legs;
    private Boolean reused;
    private Boolean landing_attempt;
    private Boolean landing_success;
    private String landing_type;
    private String landpad;
  }
}
