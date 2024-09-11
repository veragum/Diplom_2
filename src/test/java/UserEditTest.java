import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UserEditTest {
    private UserSteps userSteps = new UserSteps();
    private String email = "super-morkovka@yandex.ru";
    private String password = "ffgffgfgggf";
    private String name = "petya";

    @Before
    public void createUniqueUser() {
        userSteps
                .createUser(email, password, name)
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void userEmailEditNotOk() {
        userSteps
                .userDataEdit("email", password, name, "")
                .statusCode(401)
                .body("message", is("You should be authorised"));
    }

    @Test
    public void userPasswordEditNotOk() {
        userSteps
                .userDataEdit(email, "password", name, "")
                .statusCode(401)
                .body("message", is("You should be authorised"));
    }

    @Test
    public void userNameEditNotOk() {
        userSteps
                .userDataEdit(email, password, "name", "")
                .statusCode(401)
                .body("message", is("You should be authorised"));
    }

    @Test
    public void userEmailEditOk() {
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        userSteps
                .userDataEdit("email", password, name, token)
                .statusCode(200)
                .body("success", is(true));
        userSteps
                .userDataEdit(email, password, name, token)
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void userPasswordEditOk() {
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        userSteps
                .userDataEdit(email, "password", name, token)
                .statusCode(200)
                .body("success", is(true));
        userSteps
                .userDataEdit(email, password, name, token)
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void userNameEditOk() {
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        userSteps
                .userDataEdit(email, password, "name", token)
                .statusCode(200)
                .body("success", is(true));
        userSteps
                .userDataEdit(email, password, name, token)
                .statusCode(200)
                .body("success", is(true));
    }

    @After
    public void tearDown() {
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }
}
