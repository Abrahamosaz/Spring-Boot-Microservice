import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PatientIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:4003/api";
    }


    @Test
    public void shouldReturnPatientWithValidToken() {
        String loginPayload = """
                    {
                        "email": "admin@example.com",
                        "password": "password123"
                    }
                """;

        String accessToken = RestAssured.given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", Matchers.notNullValue())
                .extract()
                .jsonPath()
                .getString("accessToken");


        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/patients")
                .then()
                .statusCode(200)
                .body("$", Matchers.instanceOf(List.class));

    }
}
