package com.example.demo.dtos.responses;

import com.dan.shoe.shoe.models.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {
    Long id;
    Product product;
    int size;
    String color;
    int stockQuantity;
    int price;
    int discountRate;
    int priceAfterDiscount;
    boolean defaultVariant;
    String imageAvatar;
    List<String> imageOthers;
    String qrCode;
}
