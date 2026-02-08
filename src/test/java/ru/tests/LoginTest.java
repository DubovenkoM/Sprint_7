package ru.tests;

import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.api.models.Courier;
import ru.api.models.CourierCredentials;
import ru.clients.CourierClient;
import ru.utils.DataGenerator;

import static org.hamcrest.Matchers.*;

@DisplayName("Тесты авторизации курьера")
public class LoginTest {

    private CourierClient courierClient = new CourierClient();
    private Courier testCourier;

    @AfterEach
    public void tearDown() {
        if (testCourier != null) {
            try {
                // Удаляем созданного курьера
                Response loginResponse = courierClient.loginCourier(
                        new CourierCredentials(
                                testCourier.getLogin(),
                                testCourier.getPassword()
                        )
                );

                if (loginResponse.getStatusCode() == 200) {
                    int courierId = loginResponse.jsonPath().getInt("id");
                    courierClient.deleteCourier(courierId);
                }
            } catch (Exception e) {
                // Игнорируем
            }
        }
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void courierCanLoginTest() {
        // Создаём курьера
        testCourier = DataGenerator.getRandomCourier();
        courierClient.createCourier(testCourier);

        CourierCredentials credentials = new CourierCredentials(
                testCourier.getLogin(),
                testCourier.getPassword()
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем возвращает ошибку")
    public void loginWithWrongPasswordReturnsErrorTest() {
        // Создаём курьера
        testCourier = DataGenerator.getRandomCourier();
        courierClient.createCourier(testCourier);

        CourierCredentials credentials = new CourierCredentials(
                testCourier.getLogin(),
                "wrong_password"
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неправильным логином возвращает ошибку")
    public void loginWithWrongLoginReturnsErrorTest() {
        // Создаём курьера
        testCourier = DataGenerator.getRandomCourier();
        courierClient.createCourier(testCourier);

        CourierCredentials credentials = new CourierCredentials(
                "wrong_login",
                testCourier.getPassword()
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация без логина возвращает ошибку")
    public void loginWithoutLoginReturnsErrorTest() {
        CourierCredentials credentials = new CourierCredentials(null, "password");

        Response response = courierClient.loginCourier(credentials);

        // Проверяем что это не успех (не 2xx)
        response.then()
                .statusCode(not(both(greaterThanOrEqualTo(200)).and(lessThan(300))));
    }

    @Test
    @DisplayName("Авторизация без пароля возвращает ошибку")
    public void loginWithoutPasswordReturnsErrorTest() {
        CourierCredentials credentials = new CourierCredentials("login", null);

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(not(both(greaterThanOrEqualTo(200)).and(lessThan(300))));
    }

    @Test
    @DisplayName("Авторизация несуществующего пользователя возвращает ошибку")
    public void loginNonExistentUserReturnsErrorTest() {
        CourierCredentials credentials = new CourierCredentials(
                "nonexistent_user",
                "password123"
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Успешная авторизация возвращает id")
    public void successfulLoginReturnsIdTest() {
        // Создаём курьера
        testCourier = DataGenerator.getRandomCourier();
        courierClient.createCourier(testCourier);

        CourierCredentials credentials = new CourierCredentials(
                testCourier.getLogin(),
                testCourier.getPassword()
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .body("id", greaterThan(0));
    }
}