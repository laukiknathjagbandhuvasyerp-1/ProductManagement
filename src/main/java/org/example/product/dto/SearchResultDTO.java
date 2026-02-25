package org.example.product.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResultDTO {

    private String displayText;
    private Long productId;
    private Long variantId;

}
