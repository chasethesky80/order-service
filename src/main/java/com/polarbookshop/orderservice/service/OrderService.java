package com.polarbookshop.orderservice.service;

import com.polarbookshop.orderservice.domain.Order;
import com.polarbookshop.orderservice.domain.OrderStatus;
import com.polarbookshop.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(final String isbn, final int quantity) {
        return orderRepository.save(buildRejectedOrder(isbn, quantity));
    }

    /**
     * Build a rejected order from the isbn and quantity
     * @param bookIsbn
     * @param quantity
     * @return
     */
    private Order buildRejectedOrder(final String bookIsbn, final int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
