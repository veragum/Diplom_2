import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    private static final String HOST = "https://stellarburgers.nomoreparties.site";
    private static final String ORDER = "/api/orders";
    private static final String INGREDIENTS = "api/ingredients";
    private static final String GET_ORDER = "/api/orders";


    @Step
    public ValidatableResponse createOrder(String token, List<String> ingredients) {

        OrderRequest orderRequest = new OrderRequest(ingredients);
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .baseUri(HOST)
                .body(orderRequest)
                .when()
                .post(ORDER)
                .then();
    }

    @Step
    public List<String> getIngredients() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOST)
                .when().get(INGREDIENTS)
                .then()
                .extract().path("data._id");
    }

    @Step
    public ValidatableResponse getOrder(String token) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOST)
                .header("Authorization", token)
                .when().get(GET_ORDER)
                .then();
    }


}
