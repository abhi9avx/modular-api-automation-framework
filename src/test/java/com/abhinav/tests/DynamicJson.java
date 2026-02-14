package com.abhinav.tests;

import static io.restassured.RestAssured.*;

import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DynamicJson {
  private static Map<String, String> bookIds = new HashMap<>();

  @Test(dataProvider = "BookData", priority = 1)
  public void createBook(String aisle, String isbn) {
    RestAssured.baseURI = "http://216.10.245.166";
    String response =
        given()
            .log()
            .all()
            .header("Content-Type", "application/json")
            .body(payload.AddBook(aisle, isbn))
            .when()
            .post("/Library/Addbook.php")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();

    JsonPath js = new JsonPath(response);
    String id = js.get("ID");
    bookIds.put(aisle + isbn, id);
    System.out.println("Created ID: " + id);
  }

  @Test(dataProvider = "BookData", priority = 2)
  public void deleteBook(String aisle, String isbn) throws InterruptedException {
    String id = bookIds.get(aisle + isbn);

    // Pause to ensure the record is committed in the backend database
    Thread.sleep(1000);

    RestAssured.baseURI = "http://216.10.245.166";

    String response =
        given()
            .log()
            .all()
            .header("Content-Type", "application/json")
            .body(payload.DeleteBook(id))
            .when()
            .post("/Library/DeleteBook.php")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();

    System.out.println("Raw Delete Response: " + response);

    if (response != null && !response.trim().isEmpty()) {
      JsonPath js = new JsonPath(response);
      String msg = js.get("msg");
      System.out.println("Delete Message: " + msg);
    } else {
      System.out.println("Delete successful (Empty response body)");
    }
  }

  // parametrisation of data

  @DataProvider(name = "BookData")
  public Object[][] getData() {
    return new Object[][] {{"abhinav", "240227"}};
  }
}
