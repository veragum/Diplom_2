import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
    private UserSteps userSteps = new UserSteps();
    private String email = "super-morkovka@yandex.ru";
    private String password = "ffgffgfgggf";
    private String name = "petya";


    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUser() {
        ValidatableResponse user = userSteps
                .createUser(email, password, name);
        user.statusCode(200)
                .body("success", is(true));
    }


    @Test
    @DisplayName("Создание ранее зарегистрированного пользователя")
    public void duplicateUserNotAllowed() {
        userSteps
                .createUser(email, password, name)
                .statusCode(200);

        userSteps
                .createUser(email, password, name)
                .statusCode(403)
                .body("message", is("User already exists"));

    }

    @Test
    @DisplayName("Создать пользователя с отсутствующим обязательным полем")
    public void mandatoryFieldsShouldBeFilled() {
        userSteps
                .createUser("", password, name)
                .statusCode(403)
                .body("message", is("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        String token = userSteps.loginUser(email, password, name)
                .extract().body().path("accessToken");
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }
}
