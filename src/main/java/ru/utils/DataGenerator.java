package ru.utils;

import ru.api.models.Courier;
import ru.api.models.Order;
import java.util.*;

public class DataGenerator {

    public static Courier getRandomCourier() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        return new Courier(
                "courier_" + unique,
                "password_" + unique,
                "Name_" + unique
        );
    }

    public static Courier getCourierWithoutField(String missingField) {
        String login = "test_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "password123";
        String firstName = "TestName";

        switch (missingField) {
            case "login":
                return new Courier(null, password, firstName);
            case "password":
                return new Courier(login, null, firstName);
            case "firstName":
                return new Courier(login, password, null);
            default:
                return new Courier(login, password, firstName);
        }
    }

    public static Order getRandomOrder(List<String> colors) {
        // Дата на 7 дней вперед
        java.time.LocalDate futureDate = java.time.LocalDate.now().plusDays(7);

        return new Order(
                "Иван",
                "Иванов",
                "ул. Ленина, д. 10",
                1,  // станция метро
                "+79998887766",
                3,  // дней аренды
                futureDate.toString(),  // дата доставки
                "Комментарий к заказу",
                colors
        );
    }
}