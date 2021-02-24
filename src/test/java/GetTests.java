import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class GetTests extends APITestCase {

    @Test
    public void getUniqueBook() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/1")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, response.jsonPath().getInt("id"));
        Assertions.assertEquals("John Smith", response.jsonPath().getString("author"));
        Assertions.assertEquals("Reliability of late night deployments", response.jsonPath().getString("title"));
    }

    @Description("Verify created book is added")
    @Test
    public void getCreatedBook() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/3")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(3, response.jsonPath().getInt("id"));
        Assertions.assertEquals("test author", response.jsonPath().getString("author"));
        Assertions.assertEquals("test title", response.jsonPath().getString("title"));
    }

    @Test
    public void getAllBook() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, response.jsonPath().getInt("firstBook.id"));
        Assertions.assertEquals("John Smith", response.jsonPath().getString("firstBook.author"));
        Assertions.assertEquals("Reliability of late night deployments", response.jsonPath().getString("firstBook.title"));
        Assertions.assertEquals(2, response.jsonPath().getInt("secondBook.id"));
        Assertions.assertEquals("Paulo Coelho", response.jsonPath().getString("secondBook.author"));
        Assertions.assertEquals("Spy", response.jsonPath().getString("secondBook.title"));

    }

    @Test
    public void getBadRequest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/111")
                .then()
                .extract().response();

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("Error 400 Bad Request", response.jsonPath().getString("error"));
    }

    @Test
    public void getInternalServerError() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_ROOT+"/500")
                .then()
                .extract().response();

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertEquals("Internal Server Error", response.jsonPath().getString("error"));
    }
}
