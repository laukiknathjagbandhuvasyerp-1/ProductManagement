package org.example.product.repo;

import org.example.product.model.ProductSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductSearchRepo extends JpaRepository<ProductSearch,Long> {

    @Query(value = """
            SELECT product_details FROM product_search 
            ORDER BY search_embedding <=> CAST(:embedding AS vector) 
            LIMIT :limit """,
            nativeQuery = true)
    List<String> findSimilar(@Param("embedding")String embedding,
                                    @Param("limit") int limit);

    @Query(value = """
            SELECT product_id,variant_id,product_details FROM product_search WHERE company_id = 2 AND merchant_id = 1 ORDER BY search_embedding <=> CAST(:embedding AS vector)
            LIMIT :limit """ , nativeQuery = true)
    List<Object[]> findSimilarProduct(@Param("embedding") String embedding,
                                    @Param("limit") int limit);

    @Transactional
    @Modifying
    @Query(value = """
        DELETE FROM product_search WHERE variant_id = :variantId """,nativeQuery = true)
    void deleteByVariantId(@Param("variantId") Long variantId);

    @Transactional
    @Modifying
    @Query(value = """
        DELETE FROM product_search WHERE product_id = :productId """,nativeQuery = true)
    void deleteByProductId(@Param("productId") Long productId);

}
