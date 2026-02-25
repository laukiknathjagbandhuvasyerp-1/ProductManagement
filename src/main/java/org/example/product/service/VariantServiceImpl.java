package org.example.product.service;

import org.example.product.dto.ProductVariantCreateDTO;
import org.example.product.dto.ProductVariantResponseDTO;
import org.example.product.model.Product;
import org.example.product.model.ProductVariant;
import org.example.product.repo.ProductRepo;
import org.example.product.repo.VariantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private VariantRepo variantRepo;

    @Autowired
    private ProductSearchEntryService productSearchEntryService;

    @Override
    public ProductVariantResponseDTO createProductVariant(ProductVariantCreateDTO dto) {

        Product product = productRepo.findById(dto.getProductId()).orElseThrow(()-> new RuntimeException("Product Not Found"));

        ProductVariant productVariant = new ProductVariant();
        productVariant.setProductVariantName(dto.getProductVariantName());
        productVariant.setProduct(product);

        ProductVariant savedVariant = variantRepo.save(productVariant);

        productSearchEntryService.addVariantsToSearch(savedVariant);

        return ProductVariantResponseDTO.builder()
                .variantId(savedVariant.getProductVariantId())
                .productId(product.getProductId())
                .variantName(savedVariant.getProductVariantName())
                .build();

    }
}
