package org.example.product.service;

import org.example.product.dto.ProductCreateDTO;
import org.example.product.dto.ProductResponseDTO;
import org.example.product.dto.ProductVariantCreateDTO;
import org.example.product.model.Product;
import org.example.product.model.ProductVariant;
import org.example.product.repo.ProductRepo;
import org.example.product.repo.ProductSearchRepo;
import org.example.product.repo.VariantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private VariantRepo variantRepo;

    @Autowired
    private ProductSearchRepo productSearchRepo;

    @Autowired
    private ProductSearchEntryService productSearchEntryService;

    @Transactional
    @Override
    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {

        Product saveProduct = Product.builder()
                .productName(productCreateDTO.getProductName())
                .productBrandName(productCreateDTO.getProductBrandName())
                .productDescription(productCreateDTO.getProductDescription())
                .build();

        Product savedProduct = productRepo.save(saveProduct);


        if (productCreateDTO.getVariants() != null && !productCreateDTO.getVariants().isEmpty()) {
            for (ProductVariantCreateDTO productVariantCreateDTO : productCreateDTO.getVariants()) {
                ProductVariant variant = ProductVariant.builder()
                        .productVariantName(productVariantCreateDTO.getProductVariantName())
                        .product(saveProduct)
                        .build();

                ProductVariant savedVariant = variantRepo.save(variant);

                productSearchEntryService.addVariantsToSearch(savedVariant);
            }
        }
        else{
            ProductVariant defaultVariant = ProductVariant.builder()
                    .productVariantName(productCreateDTO.getProductName())
                    .product(savedProduct)
                    .build();
            ProductVariant savedVariant = variantRepo.save(defaultVariant);
            productSearchEntryService.addVariantsToSearch(savedVariant);
        }

        return ProductResponseDTO.builder()
                .productId(savedProduct.getProductId())
                .productName(savedProduct.getProductName())
                .productBrandName(savedProduct.getProductBrandName())
                .productDescription(savedProduct.getProductDescription())
                .build();
    }

    @Transactional
    @Override
    public void softDeleteProduct(Long productId) {

        productSearchRepo.deleteByProductId(productId);

        Product product = productRepo.findById(productId).orElseThrow(()-> new RuntimeException("Not Found Product"));

        product.setIsDeleted(true);
        productRepo.save(product);

        for(ProductVariant productVariant : product.getProductVariantList()){
            productVariant.setIsDeleted(true);
            variantRepo.save(productVariant);
        }
    }
}
