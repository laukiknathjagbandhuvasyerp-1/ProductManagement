package org.example.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCreateDTO {

    private String productName;
    private String productBrandName;
    private String productDescription;

}
