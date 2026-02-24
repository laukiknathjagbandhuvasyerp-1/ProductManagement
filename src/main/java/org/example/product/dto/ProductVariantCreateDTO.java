package org.example.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVariantCreateDTO {

    private String productVariantName;
    private Long productId;

}
