package com.abhinav.framework.dto;

public class ExampleDto {
  private String fieldName;
  private String id;

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public static ExampleDto createDefault() {
    ExampleDto defaultDto = new ExampleDto();
    defaultDto.setId("defaultId");
    defaultDto.setFieldName("defaultFieldName");
    return defaultDto;
  }
}
