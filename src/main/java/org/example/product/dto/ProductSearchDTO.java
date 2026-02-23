package org.example.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSearchDTO {

    private long searchId;
    private long productId;
    private long variantId;
    private long companyId;
    private String type;
    private String displayText;

}
