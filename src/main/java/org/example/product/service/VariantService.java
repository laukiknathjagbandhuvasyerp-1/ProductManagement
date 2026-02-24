package org.example.product.service;

import org.example.product.model.ProductVariant;

public interface VariantService {

    ProductVariant createProductVariant(Long productId,ProductVariant productVariant);
}
