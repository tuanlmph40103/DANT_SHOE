package com.example.demo.dtos.requests;

import com.dan.shoe.shoe.models.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {
    String name;
    String description;
    int price;
    Long brandId;
    Long categoryId;
    Gender gender;

    List<ProductVariantCreationRequest> variants;
}
