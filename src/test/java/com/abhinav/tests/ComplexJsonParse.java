package com.abhinav.tests;

import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class ComplexJsonParse {

  public static void main(String[] args) {

    JsonPath js = new JsonPath(payload.CoursePrice());

    // Print the no of courses returned by API

    int count = js.getInt("courses.size()");
    System.out.println(count);

    // Print the purchase amount

    int purchaseAmount = js.getInt("dashboard.purchaseAmount");
    System.out.println(purchaseAmount);

    // print title of the first course

    String title = js.getString("courses[0].title");
    System.out.println(title);

    // print price of the third course
    int price = js.getInt("courses[2].price");
    System.out.println(price);

    // print all course title with there respective price

    for (int i = 0; i < count; i++) {
      String CoursesTitle = js.get("courses[" + i + "].title");
      int CoursesPrice = js.get("courses[" + i + "].price");
      System.out.println(CoursesTitle + " " + CoursesPrice);
    }

    // print the number of copies sold by RPA COURSE (DYNAMIC)

    for (int i = 0; i < count; i++) {
      String CoursesTitle = js.get("courses[" + i + "].title");
      if (CoursesTitle.equalsIgnoreCase("RPA")) {
        int Copies = js.get("courses[" + i + "].copies");
        System.out.println(Copies);
        break;
      }
    }

    // verify if sum of all course price matches with the purchase amount
    int totalSum = 0;
    for (int i = 0; i < count; i++) {
      int price1 = js.getInt("courses[" + i + "].price");
      int copies = js.getInt("courses[" + i + "].copies");
      totalSum = totalSum + price1 * copies;
    }
    int purchaseAmount1 = js.getInt("dashboard.purchaseAmount");

    System.out.println(totalSum);
    Assert.assertEquals(totalSum, purchaseAmount1);
  }
}
