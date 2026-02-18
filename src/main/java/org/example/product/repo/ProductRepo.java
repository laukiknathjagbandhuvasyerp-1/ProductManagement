package org.example.product.repo;

import org.example.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {


    @Query(value = "SELECT * FROM product WHERE " +
            "LOWER(product_name) LIKE LOWER(CONCAT('%', :productsearch,'%')) OR " +
            "LOWER(product_description) LIKE LOWER(CONCAT('%', :productsearch,'%')) OR " +
            "LOWER(product_brand_name) LIKE LOWER(CONCAT('%', :productsearch,'%'))",

            countQuery = "SELECT COUNT(*) FROM product WHERE "+
            "LOWER(product_name) LIKE LOWER(CONCAT('%', :productsearch,'%')) OR "+
            "LOWER(product_description) LIKE LOWER(CONCAT('%', :productsearch,'%')) OR " +
            "LOWER(product_brand_name) LIKE LOWER(CONCAT('%', :productsearch,'%'))"

            ,nativeQuery = true)
    Page<Product> searchProduct(@Param("productsearch") String product, Pageable pageable);


    @Query(value = "SELECT * FROM product WHERE " +
            "LOWER(product_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(product_brand_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY " +
            "CASE " +
            "  WHEN LOWER(product_name) LIKE LOWER(CONCAT(:keyword, '%')) THEN 1 " +
            "  WHEN LOWER(product_name) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 2 " +
            "  ELSE 3 " +
            "END, product_name " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Product> findSuggestions(@Param("keyword") String keyword,@Param("limit") int limit);


}
