package com.dan.shoe.shoe.services.impls;

import com.dan.shoe.shoe.models.*;
import com.dan.shoe.shoe.repositories.*;
import com.dan.shoe.shoe.services.CartService;
import com.dan.shoe.shoe.services.OrderService;
import com.dan.shoe.shoe.services.SeasonalDiscountService;
import com.dan.shoe.shoe.services.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private SeasonalDiscountService seasonalDiscountService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private CartService cartService;

    @Override
    public Order createOrder(String username, String voucherCode) {
        User user = userRepository.findByUsername(username);
        Order order = new Order();
        order.setUser(user);
        Cart cart = cartRepository.findByUser(user);

        // Sao chép các CartItem thành OrderItem
        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> new OrderItem(cartItem.getProductVariant(), cartItem.getQuantity(), cartItem.getPrice()))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);

        // Tính tổng giá trị ban đầu
        int originalTotal = cart.getTotalPrice();

        // Áp dụng giảm giá theo mùa (SeasonalDiscount)
        int seasonalDiscountAmount = applySeasonalDiscount(orderItems);
        order.setDiscountAmount(seasonalDiscountAmount);
        order.setTotalPrice(originalTotal - seasonalDiscountAmount);

        // Kiểm tra và áp dụng voucher nếu có
        if (!voucherCode.equals("")) {
            try {
                // Kiểm tra nếu user đã sử dụng voucher này
                Voucher voucher = voucherService.validateVoucher(voucherCode, user);
                if (voucherService.isVoucherUsedByUser(user, voucher)) {
                    throw new RuntimeException("User has already used this voucher");
                }

                int voucherDiscountAmount = applyVoucherDiscount(voucher, order.getTotalPrice());
                if (voucherDiscountAmount > 0) {
                    order.setDiscountAmount(order.getDiscountAmount() + voucherDiscountAmount);
                    order.setTotalPrice(order.getTotalPrice() - voucherDiscountAmount);
                    order.setDiscountDetails("Voucher applied: " + voucherCode);

                    // Ghi lại việc sử dụng voucher
                    voucherService.recordVoucherUsage(user, voucher);
                }
            } catch (RuntimeException e) {
                order.setDiscountDetails("Voucher error: " + e.getMessage());
            }
        }

        // Đảm bảo tổng số tiền không âm
        if (order.getTotalPrice() < 0) {
            order.setTotalPrice(0);
        }

        order.setCreatedAt(LocalDateTime.now());
        cartService.clearCart(username);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public Page<Order> getOrdersByUser(Pageable pageable, String username) {
        User user = userRepository.findByUsername(username);
        return orderRepository.findByUser(pageable, user);
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//        order.setStatus(status);
//        orderRepository.save(order);
    }

    @Override
    public void updateOrderPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaid(true);
        orderRepository.save(order);
    }

    private int applySeasonalDiscount(Set<OrderItem> orderItems) {
        int discount = 0;
        List<SeasonalDiscount> activeDiscounts = seasonalDiscountService.getActiveDiscounts();

        for (OrderItem item : orderItems) {
            for (SeasonalDiscount discountCampaign : activeDiscounts) {
                if (discountCampaign.getApplicableProducts().contains(item.getProductVariant())) {
                    discount += item.getItemPrice() * item.getQuantity() * (discountCampaign.getDiscountRate() / 100.0);
                }
            }
        }
        return discount;
    }

    private int applyVoucherDiscount(Voucher voucher, int totalPrice) {
        return voucher.isPercentage()
                ? (int) (totalPrice * (voucher.getDiscountAmount() / 100.0))
                : Math.min(voucher.getDiscountAmount(), totalPrice);
    }
}