package org.example.product.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "product_table")
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    private String productName;

    private String productDescription;

    private String productBrandName;

    @Column(columnDefinition = "vector(384)")
    private float[] productEmbedding;

    @OneToMany(mappedBy = "product", cascade =CascadeType.ALL , fetch = FetchType.LAZY,orphanRemoval = true)
    private List<ProductVariant> productVariantList;

}
