package ru.api.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.api.constants.Endpoints;
import ru.api.models.Courier;
import ru.api.models.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .body(courier)
                .post(Endpoints.CREATE_COURIER);
    }

    @Step("Логин курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .body(credentials)
                .post(Endpoints.LOGIN_COURIER);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .delete(Endpoints.DELETE_COURIER + courierId);
    }
}
