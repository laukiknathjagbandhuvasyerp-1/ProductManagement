package org.example.product.service;

import org.example.product.dto.ProductVariantCreateDTO;
import org.example.product.dto.ProductVariantResponseDTO;
import org.example.product.model.Product;
import org.example.product.model.ProductVariant;
import org.example.product.repo.ProductRepo;
import org.example.product.repo.ProductSearchRepo;
import org.example.product.repo.VariantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private VariantRepo variantRepo;

    @Autowired
    private ProductSearchEntryService productSearchEntryService;

    @Autowired
    private ProductSearchRepo productSearchRepo;

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

    @Transactional
    @Override
    public void softDeleteVariant(Long variantId) {

        ProductVariant productVariant = variantRepo.findById(variantId).orElseThrow(()-> new RuntimeException("Variant Not Found"));

        Long productId = productVariant.getProduct().getProductId();

        productSearchRepo.deleteByVariantId(variantId);

        productVariant.setIsDeleted(true);
        variantRepo.save(productVariant);

        checkAndUpdateProductStatus(productId);

    }

    private void checkAndUpdateProductStatus(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        // Count active variants (isDeleted = false)
        long activeVariantsCount = product.getProductVariantList().stream()
                .filter(v -> !v.getIsDeleted())
                .count();

        // If no active variants left, soft delete the product
        if (activeVariantsCount == 0) {
            product.setIsDeleted(true);
            productRepo.save(product);
        }
    }
}
