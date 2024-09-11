import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UserLoginTest {
    private UserSteps userSteps = new UserSteps();
    private String email = "super-morkovka@yandex.ru";
    private String password = "ffgffgfgggf";
    private String name = "petya";

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginCorrectData() {
        userSteps
                .createUser(email, password, name)
                .statusCode(200);
        userSteps
                .loginUser(email, password, name)
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginIncorrectData() {
        userSteps
                .createUser(email, password, name)
                .statusCode(200);
        userSteps
                .loginUser("email", "password", name)
                .statusCode(401)
                .body("message", is("email or password are incorrect"));
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
