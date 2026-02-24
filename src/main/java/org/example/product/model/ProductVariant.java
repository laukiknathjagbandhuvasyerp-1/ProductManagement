package org.example.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Table(name = "product_variant")
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productVariantId;

    private String productVariantName;

    @Column(columnDefinition = "vector(384)")
    private float[] variantEmbedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id" , referencedColumnName = "productId")
    @JsonIgnore
    private Product product;
}
