package org.example.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVariantResponseDTO {

    private Long variantId;
    private String variantName;
    private Long productId;

}
