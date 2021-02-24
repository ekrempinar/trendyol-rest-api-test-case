import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PutTests extends APITestCase {

    @Description("Verify that title and author are required fields")
    @Test
    public void putRequestWithOnlyAuthor() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(API_ROOT + "/?author=ekrem")
                .then()
                .extract().response();

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("Field title is required", response.jsonPath().getString("error"));
    }

    @Test
    public void putRequestWithOnlyTitle() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(API_ROOT + "/?title=New book")
                .then()
                .extract().response();

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("Field author is required", response.jsonPath().getString("error"));
    }


    @Description(" Verify that title and author cannot be empty")
    @Test
    public void putBadRequestWithEmptyAuthor() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(API_ROOT+"/?author&title=There are a title")
                .then()
                .extract().response();

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("Field author can not be empty", response.jsonPath().getString("error"));
    }
    @Test
    public void putBadRequestWithEmptyTitle() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(API_ROOT+"/?author=There are an author&title=")
                .then()
                .extract().response();

        Assertions.assertEquals(204, response.statusCode());
        Assertions.assertEquals("Field title can not be empty", response.jsonPath().getString("error"));
    }
    @Test
    public void putBadRequestWithNoParameter() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(API_ROOT)
                .then()
                .extract().response();

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("Field author and title is required", response.jsonPath().getString("error"));
    }


    @Description("Verify that the id field is read−only")
    @Test
    public void putBadRequestWithId() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(API_ROOT+"/23")
                .then()
                .extract().response();

        Assertions.assertEquals(405, response.statusCode());
        Assertions.assertEquals("id field is read only", response.jsonPath().getString("error"));
    }


    @Description("Verify that you cannot create a duplicate book.")
    @Test
    public void putBadRequestForDuplicated() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(API_ROOT+"/?author=John Smith&title=Reliability of late night deployments")
                .then()
                .extract().response();

        Assertions.assertEquals(405, response.statusCode());
        Assertions.assertEquals("Another book with similar author and title already exists", response.jsonPath().getString("error"));
    }
    @Test
    public void putInternalServerError() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .put(API_ROOT+"/500")
                .then()
                .extract().response();

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertEquals("Internal Server Error", response.jsonPath().getString("error"));
    }

    @Description("Verify that you can create a new book via PUT")
    @Test
    public void putCreatedBook() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .put(API_ROOT+"?id=3&author=test author&title=test title")
                .then()
                .extract().response();

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("New book created", response.jsonPath().getString("message"));
    }
}
