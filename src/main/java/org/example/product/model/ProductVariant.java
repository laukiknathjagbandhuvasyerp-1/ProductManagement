package org.example.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id" , referencedColumnName = "productId")
    @JsonIgnore
    private Product product;

    @OneToMany(mappedBy = "productVariant" , cascade = CascadeType.ALL)
    private List<ProductSearch> productSearches;
}
