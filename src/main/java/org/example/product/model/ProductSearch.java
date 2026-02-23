package org.example.product.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_search")
public class ProductSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long searchId;

    private long variantId;

    private long companyId;

    @Column(nullable = false)
    private long productId;

    private String productDetails;

    @Column(columnDefinition = "vector(384)")
    private float[] searchEmbedding;

}
