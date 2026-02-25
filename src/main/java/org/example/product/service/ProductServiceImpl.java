package org.example.product.service;

import org.example.product.dto.ProductCreateDTO;
import org.example.product.dto.ProductResponseDTO;
import org.example.product.dto.ProductVariantCreateDTO;
import org.example.product.model.Product;
import org.example.product.model.ProductVariant;
import org.example.product.repo.ProductRepo;
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
    private ProductSearchEntryService productSearchEntryService;

//    @Override
//    public Page<Product> searchProducts(String product, int page) {
//        Pageable pageable = PageRequest.of(page-1,PAGE_SIZE);
//        if(product == null || product.trim().isEmpty()){
//            return productRepo.findAll(pageable);
//        }
//        return productRepo.searchProduct(product.trim(),pageable);
//    }
//
//    @Override
//    public List<Product> findSuggestions(String product, int limit) {
//        if(product == null|| product.trim().isEmpty()){
//            return Collections.emptyList();
//        }
//        return productRepo.findSuggestions(product.trim(),limit);
//    }

    @Transactional
    @Override
    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {

        Product saveProduct = Product.builder()
                .productName(productCreateDTO.getProductName())
                .productBrandName(productCreateDTO.getProductBrandName())
                .productDescription(productCreateDTO.getProductDescription())
                .build();

        Product savedProduct = productRepo.save(saveProduct);

//        List<ProductVariant> savedVariants = new ArrayList<>();

        if (productCreateDTO.getVariants() != null && !productCreateDTO.getVariants().isEmpty()) {
            for (ProductVariantCreateDTO productVariantCreateDTO : productCreateDTO.getVariants()) {
                ProductVariant variant = ProductVariant.builder()
                        .productVariantName(productVariantCreateDTO.getProductVariantName())
                        .product(saveProduct)
                        .build();

                ProductVariant savedVariant = variantRepo.save(variant);
//                savedVariants.add(savedVariant);

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
}
