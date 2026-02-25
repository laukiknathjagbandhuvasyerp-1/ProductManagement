package org.example.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "product_variant")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productVariantId;

    private String productVariantName;

//    @Column(columnDefinition = "vector(384)")
//    private float[] variantEmbedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id" , referencedColumnName = "productId")
    @JsonIgnore
    private Product product;
}
