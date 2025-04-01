import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AuthIntegrationTest {


    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:4003/api";
    }


    @Test
    public void shouldReturnOkWithValidToken() {
        String loginPayload = """
                    {
                        "email": "admin@example.com",
                        "password": "password123"
                    }
                """;

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", Matchers.notNullValue())
                .extract()
                .response();

        System.out.println("Generated token " + response.jsonPath().getString("accessToken"));
    }


    @Test
    public void shouldReturnUnauthorizedWithInvalidCredentails() {
        String loginPayload = """
                    {
                        "email": "admin@example.com",
                        "password": "password12"
                    }
                """;

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("message", Matchers.notNullValue())
                .extract()
                .response();

        String errorMessage = response.jsonPath().getString("message");
        Assertions.assertEquals("Invalid credentials", errorMessage);
    }
}
