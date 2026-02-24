package org.example.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDTO {

    private Long productId;
    private String productName;
    private String productBrandName;
    private String productDescription;

}
