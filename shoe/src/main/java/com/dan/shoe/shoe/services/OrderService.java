package com.dan.shoe.shoe.services;

import com.dan.shoe.shoe.models.Cart;
import com.dan.shoe.shoe.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order createOrder(String username, String voucherCode);
    Order getOrderById(Long id);
    Page<Order> getOrdersByUser(Pageable pageable, String username);
    void updateOrderStatus(Long orderId, String status);
    void updateOrderPaid(Long orderId);
}
