import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserSteps {
    private static final String HOST = "https://stellarburgers.nomoreparties.site";
    private static final String USER = "/api/auth/register";
    private static final String LOGIN = "/api/auth/login";
    private static final String EDIT = "/api/auth/user";
    private static final String DELETE = "/api/auth/user";

    @Step
    public ValidatableResponse createUser(String email, String password, String name) {
        return given().log().ifValidationFails()
                .contentType(ContentType.JSON)
                .baseUri(HOST)
                .body("{\n" +
                        "    \"email\": \"" + email + "\",\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"name\": \"" + name + "\"\n" +
                        "}")
                .when()
                .post(USER)
                .then();
    }


    @Step
    public ValidatableResponse loginUser(String email, String password) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOST)
                .body("{\n" +
                        "    \"email\": \"" + email + "\",\n" +
                        "    \"password\": \"" + password + "\"\n" +
                        "}")
                .when()
                .post(LOGIN).then();
    }

    @Step
    public ValidatableResponse userDataEdit(String email, String password, String name, String token) {
        return given()
                .header("Authorization", token)
                .baseUri(HOST)
                .body("{\n" +
                        "    \"email\": \"" + email + "\",\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"name\": \"" + name + "\"\n" +
                        "}")
                .when()
                .patch(EDIT)
                .then();
    }


    @Step
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .baseUri(HOST)
                .when()
                .delete(DELETE)
                .then();
    }
}


