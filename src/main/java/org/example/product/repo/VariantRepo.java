package org.example.product.repo;

import org.example.product.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VariantRepo extends JpaRepository<Variant,Long> {

    @Query(value="SELECT v.* , p.product_name, p.product_brand_name " +
            "FROM product_variant v " +
            "JOIN product_table p ON " +
            "v.product_id = p.product_id " +
            "ORDER BY v.variant_embedding <=> CAST(:embedding AS vector) " +
            "LIMIT :limit",nativeQuery = true)

    List<Map<String,String>> findSimilarVariants(@Param("embedding")String embedding, @Param("limit") int limit);

}
