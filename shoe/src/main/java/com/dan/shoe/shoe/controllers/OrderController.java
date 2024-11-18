package com.dan.shoe.shoe.controllers;

import com.dan.shoe.shoe.dtos.responses.VNPayMessage;
import com.dan.shoe.shoe.models.Order;
import com.dan.shoe.shoe.security.jwt.JwtService;
import com.dan.shoe.shoe.services.OrderService;
import com.dan.shoe.shoe.services.impls.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/create")
    public ResponseEntity<VNPayMessage> createOrder(HttpServletRequest request,
                                                    @RequestParam(value = "voucherCode", defaultValue = "") String voucherCode) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        Order order = orderService.createOrder(username, voucherCode);

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(order.getTotalPrice(), order.getId().toString(), baseUrl);

        VNPayMessage VNPayMessage = new VNPayMessage("payment", vnpayUrl);
        return ResponseEntity.ok(VNPayMessage);
    }

    // API để lấy thông tin đơn hàng theo ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // API để lấy tất cả đơn hàng của một user
    @GetMapping("/my-orders")
    public ResponseEntity<Page<Order>> getOrdersByUser(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "createdAt") String sortBy,
                                                       @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.ofSize(size).withPage(page).withSort(Sort.by(Sort.Direction.fromString(order), sortBy));
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        try {
            Page<Order> orders = orderService.getOrdersByUser(pageable, username);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }
}
