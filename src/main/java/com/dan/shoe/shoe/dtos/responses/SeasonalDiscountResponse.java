package com.dan.shoe.shoe.dtos.responses;

import com.dan.shoe.shoe.models.ProductVariant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeasonalDiscountResponse {
    Long id;
    String name;
    int discountRate;
    LocalDate startDate;
    LocalDate endDate;
    String description;
    List<ProductVariant> applicableProducts;
    boolean status;
}
