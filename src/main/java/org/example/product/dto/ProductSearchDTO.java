package org.example.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchDTO {

    private long searchId;
    private long productId;
    private long variantId;
    private long companyId;
    private String type;
    private String displayText;

}
