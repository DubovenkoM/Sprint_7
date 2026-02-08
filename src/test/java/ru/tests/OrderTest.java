package ru.tests;

import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.api.models.Order;
import ru.clients.OrderClient;
import ru.utils.DataGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;

@DisplayName("Тесты заказов")
public class OrderTest {

    private OrderClient orderClient = new OrderClient();

    //данные для теста
    private static Stream<List<String>> colorProvider() {
        return Stream.of(
                Arrays.asList("BLACK"),
                Arrays.asList("GREY"),
                Arrays.asList("BLACK", "GREY"),
                Arrays.asList()  // без цвета
        );
    }

    @ParameterizedTest(name = "Цвета: {0}")
    @MethodSource("colorProvider")
    @DisplayName("Создание заказа с разными цветами")
    public void createOrderWithDifferentColorsTest(List<String> colors) {
        // colors приходит из метода colorProvider()
        Order order = DataGenerator.getRandomOrder(colors);

        Response response = orderClient.createOrder(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());

        int track = response.jsonPath().getInt("track");
        System.out.println("Создан заказ #" + track + " с цветами: " + colors);
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersListReturnsOrdersTest() {
        Response response = orderClient.getOrdersList();

         response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", isA(List.class));

        int orderCount = response.jsonPath().getList("orders").size();
        System.out.println("В списке " + orderCount + " заказов");
    }
}