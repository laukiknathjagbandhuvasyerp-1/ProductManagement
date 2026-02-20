package org.example.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import com.pgvector.PGvector;

@Entity
@Data
@Table(name = "product_variant")
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productVariantId;

    private String productVariantName;

    @Column(columnDefinition = "vector(384)")
    private float[] variantEmbedding;

    @ManyToOne
    @JoinColumn(name = "product_id" , referencedColumnName = "productId")
    @JsonIgnore
    private Product product;
}
