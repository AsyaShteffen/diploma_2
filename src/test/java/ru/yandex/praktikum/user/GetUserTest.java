package ru.yandex.praktikum.user;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import ru.yandex.praktikum.objects.User;
import ru.yandex.praktikum.api.UserClient;
import ru.yandex.praktikum.services.UserGenerator;
import org.apache.commons.lang3.StringUtils;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Получение пользователя")
public class GetUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private static final String MESSAGE_UNAUTHORIZED = "You should be authorised";
    private static final String INVALID_TOKEN = "dlfkjhdfkgjhdfkgjhdfkghfkgjhd";


    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void clearState() {
        userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }

    @Test
    @DisplayName("Получение пользователя по валидным кредам")
    public void getUserByValidCredentials() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.getUser(accessToken);
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");
        String email = response.extract().path("user.email");
        String name = response.extract().path("user.name");

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("User is get incorrect", isGet, equalTo(true));
        assertThat("Email not equal", email, equalTo(user.getEmail()));
        assertThat("Name not equal", name, equalTo(user.getName()));
    }

    @Test
    @DisplayName("Получение пользователя по невалидным кредам")
    public void getUserByInvalidCredentials() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.getUser(INVALID_TOKEN);
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");
        String message = response.extract().path("message");


        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo(MESSAGE_UNAUTHORIZED));
        assertThat("Order is get correct", isGet, equalTo(false));
    }
}