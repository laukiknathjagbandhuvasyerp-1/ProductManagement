package org.example.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "product_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private String productDescription;

    private String productBrandName;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "product", cascade =CascadeType.ALL , fetch = FetchType.LAZY,orphanRemoval = true)
    private List<ProductVariant> productVariantList;

}
