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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchId;

    private Long companyId;

    private Long merchantId;

    @Column(nullable = false)
    private Long productId;

    private String productDetails;

    @Column(columnDefinition = "vector(384)")
    private float[] searchEmbedding;

    @ManyToOne
    @JoinColumn(name="variant_id",nullable = false)
    private ProductVariant productVariant;

}
