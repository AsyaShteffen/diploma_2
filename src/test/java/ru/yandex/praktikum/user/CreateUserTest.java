package ru.yandex.praktikum.user;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import ru.yandex.praktikum.objects.User;
import ru.yandex.praktikum.api.UserClient;
import ru.yandex.praktikum.services.UserGenerator;
import org.apache.commons.lang3.StringUtils;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Создание пользователя")
public class CreateUserTest {
    private static final String MESSAGE_FORBIDDEN = "User already exists";
    private static final String MESSAGE_FORBIDDEN_EMPTY_FIELD = "Email, password and name are required fields";
    private ValidatableResponse response;
    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание пользователя с валидными кредами")
    public void createUserByValidCredentials() {
        response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        String accessToken = response.extract().path("accessToken");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("User is create incorrect", isCreate, equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя с пустой почтой")
    public void createUserWithEmptyEmail() {
        user.setEmail(null);
        response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Message not equal", message, equalTo(MESSAGE_FORBIDDEN_EMPTY_FIELD));
        assertThat("User is create correct", isCreate, equalTo(false));

        String accessToken = response.extract().path("accessToken");
        if(accessToken != null) {
            userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
        }
    }

    @Test
    @DisplayName("Создание пользователя с пустым паролем")
    public void createUserWithEmptyPassword() {
        user.setPassword(null);
        response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Message not equal", message, equalTo(MESSAGE_FORBIDDEN_EMPTY_FIELD));
        assertThat("User is create correct", isCreate, equalTo(false));

        String accessToken = response.extract().path("accessToken");
        if(accessToken != null) {
            userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
        }
    }

    @Test
    @DisplayName("Создание пользователя с пустым именем")
    public void createUserWithEmptyName() {
        user.setName(null);
        response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Message not equal", message, equalTo(MESSAGE_FORBIDDEN_EMPTY_FIELD));
        assertThat("User is create correct", isCreate, equalTo(false));

        String accessToken = response.extract().path("accessToken");
        if(accessToken != null) {
            userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
        }
    }

    @Test
    @DisplayName("Повторное создание пользователя")
    public void repeatedRequestByCreateUser() {
        userClient.createUser(user);
        response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("Message not equal", message, equalTo(MESSAGE_FORBIDDEN));
        assertThat("User is create correct", isCreate, equalTo(false));

        String accessToken = response.extract().path("accessToken");
        if(accessToken != null) {
            userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
        }
    }
}