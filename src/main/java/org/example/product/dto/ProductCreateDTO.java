package org.example.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDTO {

    private String productName;
    private String productBrandName;
    private String productDescription;

    private List<ProductVariantCreateDTO> variants;

}
