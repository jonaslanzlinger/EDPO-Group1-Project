package ch.unisg.order.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a domain class for OrderRegistry.
 * It is a singleton class that stores the list of orders that have been placed.
 */
@Component
public class OrderRegistry {

    /**
     * The list of orders that have been placed.
     */
    @Getter
    private static final List<Order> orders = new ArrayList<>();

    /**
     * @param order The order to be added to the registry.
     */
    public static void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * @param order The order to be removed from the registry.
     */
    public static void removeOrder(Order order) {
        orders.remove(order);
    }

    /**
     * @param orderId The order ID of the order from the registry.
     */
    public static Order getOrderById(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }
}
