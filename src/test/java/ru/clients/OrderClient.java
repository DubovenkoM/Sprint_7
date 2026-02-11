package ru.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.api.constants.Endpoints;
import ru.api.models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .body(order)
                .post(Endpoints.CREATE_ORDER);
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .queryParam("limit", 30)
                .queryParam("page", 0)
                .get(Endpoints.GET_ORDERS_LIST);
    }
}
