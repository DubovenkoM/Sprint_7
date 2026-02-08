package ru.tests;

import io.restassured.RestAssured;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.clients.CourierClient;
import ru.api.models.Courier;
import ru.api.models.CourierCredentials;
import ru.utils.DataGenerator;

public class BaseTest {

    protected CourierClient courierClient;
    protected Courier testCourier;
    protected int testCourierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierClient = new CourierClient();
    }

    protected void createTestCourier() {
        testCourier = DataGenerator.getRandomCourier();
        courierClient.createCourier(testCourier);
    }

    protected int loginAndGetCourierId(Courier courier) {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        return courierClient.loginCourier(credentials)
                .then()
                .extract()
                .path("id");
    }

    @AfterEach
    public void cleanUp() {
        if (testCourier != null) {
            try {
                int courierId = loginAndGetCourierId(testCourier);
                if (courierId > 0) {
                    courierClient.deleteCourier(courierId);
                }
            } catch (Exception e) {
                System.out.println("Не удалось удалить курьера: " + e.getMessage());
            }
        }
    }
}
