package org.example.product.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "product_table_new")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    private String productName;

    private String productDescription;

    private String productBrandName;


    @OneToMany(mappedBy = "product", cascade =CascadeType.ALL)
    private List<Variant> variantList;

}
