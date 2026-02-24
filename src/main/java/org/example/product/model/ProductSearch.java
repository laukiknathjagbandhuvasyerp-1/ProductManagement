package org.example.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "product_search")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long searchId;

    private Long variantId;

    private long companyId;

    @Column(nullable = false)
    private long productId;

    private String productDetails;

    @Column(columnDefinition = "vector(384)")
    private float[] searchEmbedding;

}
