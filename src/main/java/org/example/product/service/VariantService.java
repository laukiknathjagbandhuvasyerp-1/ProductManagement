package org.example.product.service;

import org.example.product.dto.ProductVariantCreateDTO;
import org.example.product.dto.ProductVariantResponseDTO;
import org.example.product.model.ProductVariant;

public interface VariantService {

    ProductVariantResponseDTO createProductVariant(ProductVariantCreateDTO productVariant);

    void softDeleteVariant(Long productId);

}
