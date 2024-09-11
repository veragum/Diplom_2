import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersTest {
    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();
    private String email = "super-morkovka@yandex.ru";
    private String password = "ffgffgfgggf";
    private String name = "petya";

    @Before
    public void createUserAndOrder() {
        userSteps
                .createUser(email, password, name)
                .statusCode(200);
        List<String> ingredients = orderSteps.getIngredients();
        Collections.shuffle(ingredients);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        orderSteps
                .createOrder(token, List.of(ingredients.get(0), ingredients.get(1)))
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrderFromAuthUser() {
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        orderSteps
                .getOrder(token)
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrderFromNotAuthUser() {
        orderSteps
                .getOrder("")
                .statusCode(401)
                .body("message", is("You should be authorised"));
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
