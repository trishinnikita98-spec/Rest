
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // ← ДОБАВЬТЕ ЭТУ АННОТАЦИЮ
public class RestTest {

    private static String jsessionId;

    @BeforeAll
    static void initSession() {
        jsessionId =
                given()
                        .baseUri("http://localhost:8080")
                        .when()
                        .get("/food")
                        .then()
                        .statusCode(200)
                        .extract()
                        .cookie("JSESSIONID");

        System.out.println("Получен JSESSIONID = " + jsessionId);
    }


    @Test
    @Order(1)
    void PostExoticFruit() {
        String requestBody = "{" +
            "\"name\": \"Дьявольский фрукт\"," +
            "\"type\": \"FRUIT\"," +
            "\"exotic\": true" +
            "}";
        Response response = given()
            .baseUri("http://localhost:8080")
            .basePath("/api/food")
            .cookie("JSESSIONID", jsessionId)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .log().all()
            .post();
        given()
                .baseUri("http://localhost:8080")
                .basePath("/api/food")
                .cookie("JSESSIONID", jsessionId)
                .accept("application/json")
                .when()
                .log().all()
                .get()
                .then()
                .log().all()
                .statusCode(200)
                .body("name", hasItem("Дьявольский фрукт"));
    }

    @Test
    @Order(2)
    void GetFood() {
        given()
                .baseUri("http://localhost:8080")
                .basePath("/api/food")
                .cookie("JSESSIONID", jsessionId)
                .accept("application/json")
                .when()
                .log().all()
                .get()
                .then()
                .log().all()
                .statusCode(200);
    }
}

