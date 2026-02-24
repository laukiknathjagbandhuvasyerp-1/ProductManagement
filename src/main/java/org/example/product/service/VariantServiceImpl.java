package org.example.product.service;

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
    public ProductVariant createProductVariant(Long productId, ProductVariant productVariant) {

        Product product = productRepo.findById(productId).orElseThrow(()-> new RuntimeException("Product Not Found"));

        productVariant.setProduct(product);

        ProductVariant savedVariant = variantRepo.save(productVariant);

        productSearchEntryService.addVariantsToSearch(savedVariant);

        return savedVariant;
    }
}
