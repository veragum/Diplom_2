import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();
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
    @DisplayName("Создание заказа с ингридиентами авторизованным пользователем")
    public void createOrderAuthWithIngredients() {
        List<String> ingredients = orderSteps.getIngredients();
        Collections.shuffle(ingredients);
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        orderSteps
                .createOrder(token, List.of(ingredients.get(0), ingredients.get(1)))
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов авторизованным пользователем")
    public void createOrderAuthWithoutIngredients() {
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        orderSteps
                .createOrder(token, new ArrayList<>())
                .statusCode(400)
                .body("message", is("Ingredient ids must be provided"));
    }

    // Известный баг: Только авторизованные пользователи могут делать заказы.
    // По документации в респонсе должен быть редирект на маршрут  /login .
    @Test
    @DisplayName("Создание заказа с ингридиентами неавторизованным пользователем")
    public void createOrderNotAuthWithIngredients() {
        List<String> ingredients = orderSteps.getIngredients();
        Collections.shuffle(ingredients);
        orderSteps
                .createOrder("", List.of(ingredients.get(0), ingredients.get(1)))
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов неавторизованным пользователем")
    public void createOrderNotAuthWithoutIngredients() {
        orderSteps
                .createOrder("", new ArrayList<>())
                .statusCode(400)
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderAuthWithIncorrectHash() {
        String token = userSteps.loginUser(email, password)
                .extract().body().path("accessToken");
        String response = orderSteps
                .createOrder(token, List.of("dfdgr"))
                .statusCode(500)
                .extract().body().asPrettyString();
        Assert.assertTrue(response.contains("Internal Server Error"));
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
