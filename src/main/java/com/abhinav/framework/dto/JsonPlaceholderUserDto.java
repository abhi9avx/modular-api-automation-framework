package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonPlaceholderUserDto {

  private int id;
  private String name;
  private String username;
  private String email;
  private Address address;
  private String phone;
  private String website;
  private Company company;

  public static JsonPlaceholderUserDto createDefault() {
    return JsonPlaceholderUserDto.builder()
        .id(1)
        .name("Leanne Graham")
        .username("Bret")
        .email("Sincere@april.biz")
        .address(
            Address.builder()
                .street("Kulas Light")
                .suite("Apt. 556")
                .city("Gwenborough")
                .zipcode("92998-3874")
                .geo(Geo.builder().lat("-37.3159").lng("81.1496").build())
                .build())
        .phone("1-770-736-8031 x56442")
        .website("hildegard.org")
        .company(
            Company.builder()
                .name("Romaguera-Crona")
                .catchPhrase("Multi-layered client-server neural-net")
                .bs("harness real-time e-markets")
                .build())
        .build();
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Address {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Geo {
    private String lat;
    private String lng;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Company {
    private String name;
    private String catchPhrase;
    private String bs;
  }
}
