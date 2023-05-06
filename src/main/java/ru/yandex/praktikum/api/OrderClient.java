package ru.yandex.praktikum.api;

import io.qameta.allure.Step;
import ru.yandex.praktikum.objects.Order;
import ru.yandex.praktikum.constants.EndPoints;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends ru.yandex.praktikum.config.Spec {
    @Step("Отправляем GET запрос к /api/ingredients")
    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(EndPoints.INGREDIENTS_PATH)
                .then()
                .log().all();
    }

    @Step("Отправляем GET запрос к /api/orders")
    public ValidatableResponse getOrdersByAuthorization(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(EndPoints.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Отправляем GET запрос к /api/orders")
    public ValidatableResponse getOrdersWithoutAuthorization() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(EndPoints.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Отправляем GET запрос к /api/orders/all")
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(EndPoints.ORDER_PATH + "all")
                .then()
                .log().all();
    }

    @Step("Отправляем POST запрос к /api/orders")
    public ValidatableResponse createOrderByAuthorization(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .log().all()
                .post(EndPoints.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Отправляем POST запрос к /api/orders")
    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .log().all()
                .post(EndPoints.ORDER_PATH)
                .then()
                .log().all();
    }
}