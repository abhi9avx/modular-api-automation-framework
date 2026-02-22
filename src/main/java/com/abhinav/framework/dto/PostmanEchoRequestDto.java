package com.abhinav.framework.dto;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostmanEchoRequestDto {

  private User user;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class User {
    private String name;
    private List<String> skills;
    private Experience experience;

    public static User createDefault() {
      return User.builder()
          .name("Abhi")
          .skills(Arrays.asList("Java", "API", "Automation"))
          .experience(Experience.builder().years(3).type("SDET").build())
          .build();
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Experience {
    private Integer years;
    private String type;
  }

  public static PostmanEchoRequestDto createDefault() {
    return PostmanEchoRequestDto.builder().user(User.createDefault()).build();
  }
}
