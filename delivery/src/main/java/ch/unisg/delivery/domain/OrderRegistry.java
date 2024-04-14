package ch.unisg.delivery.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a domain class for OrderRegistry.
 * It is a singleton class that stores the list of orders that have been placed.
 * The Order List acts as a queue in a FIFO manner.
 */
@Component
public class OrderRegistry {

    /**
     * The list of orders that have been placed.
     * The Order List acts as a queue in a FIFO manner.
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
     * @return The order that is at the front of the queue.
     * @throws IndexOutOfBoundsException If there are no orders in the registry.
     */
    public static Order pop() {
        if (orders.isEmpty()) {
            return null;
        }
        return orders.remove(0);
    }

    /**
     * @param color The color of the order to be removed.
     * @return The order that is at the front of the queue with the specified color.
     */
    public static Order popNextOrderByColor(String color) {
        for (Order order : orders) {
            if (order.getOrderColor().equals(color)) {
                orders.remove(order);
                return order;
            }
        }
        return null;
    }

    public static Order peek() {
        if (orders.isEmpty()) {
            return null;
        }
        return orders.get(0);
    }
}
