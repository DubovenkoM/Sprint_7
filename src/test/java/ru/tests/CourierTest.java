package ru.tests;


import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.api.models.Courier;
import ru.clients.CourierClient;
import ru.utils.DataGenerator;
import org.junit.jupiter.api.DisplayName;

import static org.hamcrest.Matchers.*;

@DisplayName("Тесты создания курьера")
public class CourierTest {

    private CourierClient courierClient;
    private Courier testCourier;

    @BeforeEach
    public void setUp() {
        courierClient = new CourierClient();
    }

    @AfterEach
    public void tearDown() {
        if (testCourier != null) {
            try {
                // Логинимся чтобы получить ID
                Response loginResponse = courierClient.loginCourier(
                        new ru.api.models.CourierCredentials(
                                testCourier.getLogin(),
                                testCourier.getPassword()
                        )
                );

                if (loginResponse.getStatusCode() == 200) {
                    int courierId = loginResponse.jsonPath().getInt("id");
                    courierClient.deleteCourier(courierId);
                }
            } catch (Exception e) {
                // Игнорируем если курьер не создался или уже удалён
            }
        }
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void courierCanBeCreatedTest() {
        testCourier = DataGenerator.getRandomCourier();

        Response response = courierClient.createCourier(testCourier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCouriersTest() {
        testCourier = DataGenerator.getRandomCourier();

        // Первое создание
        courierClient.createCourier(testCourier)
                .then()
                .statusCode(201);

        // Второе создание с теми же данными
        Response response = courierClient.createCourier(testCourier);

        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина возвращает ошибку")
    public void createCourierWithoutLoginReturnsErrorTest() {
        Courier courier = DataGenerator.getCourierWithoutField("login");

        Response response = courierClient.createCourier(courier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля возвращает ошибку")
    public void createCourierWithoutPasswordReturnsErrorTest() {
        Courier courier = DataGenerator.getCourierWithoutField("password");

        Response response = courierClient.createCourier(courier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Успешный запрос возвращает ok: true")
    public void successfulRequestReturnsOkTrueTest() {
        testCourier = DataGenerator.getRandomCourier();

        Response response = courierClient.createCourier(testCourier);

        response.then()
                .body("ok", equalTo(true));
    }
}