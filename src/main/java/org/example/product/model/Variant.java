package org.example.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "variants")
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long variantId;

    private String variantName;

    private Integer variantQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id" , referencedColumnName = "productId")
    @JsonIgnore
    private Product product;
}
