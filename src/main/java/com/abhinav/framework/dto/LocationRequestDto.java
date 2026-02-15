package com.abhinav.framework.dto;

import java.util.List;
import lombok.*;

@Data
public class LocationRequestDto {

  private Location location;
  private int accuracy;
  private String name;
  private String phone_number;
  private String address;
  private List<String> types;
  private String website;
  private String language;

  @Data
  public static class Location {
    private double lat;
    private double lng;
  }
}

/*
 * {"location":{"lat":-38.383494,"lng":33.427362},"accuracy":50,
 * "name":"Abhinav Kumar","phone_number":"(+91) 8406938733"
 * ,"address":"ward No - 9 , Purvi Thatha , Khagaria , Bihar","types":
 * ["shoe park","shop"],"website":"http://google.com","language":"French-IN"}
 */
