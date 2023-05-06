package ru.yandex.praktikum.api;

import io.qameta.allure.Step;
import ru.yandex.praktikum.objects.User;
import ru.yandex.praktikum.constants.EndPoints;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends ru.yandex.praktikum.config.Spec {
    @Step("Отправляем GET запрос к /api/auth/user")
    public ValidatableResponse getUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(EndPoints.USER_PATH + "user")
                .then()
                .log().all();
    }

    @Step("Отправляем POST запрос к /api/auth/register")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .post(EndPoints.USER_PATH + "register")
                .then()
                .log().all();
    }

    @Step("Отправляем POST запрос к /api/auth/login")
    public ValidatableResponse loginUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .log().all()
                .post(EndPoints.USER_PATH + "login")
                .then()
                .log().all();
    }

    @Step("Отправляем POST запрос к /api/auth/logout")
    public ValidatableResponse logoutUser(String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .log().all()
                .post(EndPoints.USER_PATH + "logout")
                .then()
                .log().all();
    }

    @Step("Отправляем DELETE запрос к /api/auth/user")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .log().all()
                .delete(EndPoints.USER_PATH + "user")
                .then()
                .log().all();
    }

    @Step("Отправляем PATCH запрос к /api/auth/user")
    public ValidatableResponse updateUserByAuthorization(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .log().all()
                .patch(EndPoints.USER_PATH + "user")
                .then()
                .log().all();
    }

    @Step("Отправляем PATCH запрос к /api/auth/user")
    public ValidatableResponse updateUserWithoutAuthorization(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .patch(EndPoints.USER_PATH + "user")
                .then()
                .log().all();
    }
}