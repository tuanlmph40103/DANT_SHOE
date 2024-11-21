package com.dan.shoe.shoe.repositories;

import com.dan.shoe.shoe.models.Product;
import com.dan.shoe.shoe.models.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Set<ProductVariant> findByProduct(Product product);
    ProductVariant findByProductAndSize(Product product, int size);
    void deleteByProduct(Product product);
    Page<ProductVariant> findByProduct_NameContainingAndDefaultVariantTrue(String productName, Pageable pageable);
    Page<ProductVariant> findByProduct_NameContaining(String productName, Pageable pageable);
    @Query("SELECT DISTINCT pv.color FROM ProductVariant pv WHERE pv.product = :product")
    List<String> findDistinctColorByProduct(@Param("product") Product product);

    @Query("SELECT DISTINCT pv.size FROM ProductVariant pv WHERE pv.product = :product")
    List<Integer> findDistinctSizeByProduct(@Param("product") Product product);
}
