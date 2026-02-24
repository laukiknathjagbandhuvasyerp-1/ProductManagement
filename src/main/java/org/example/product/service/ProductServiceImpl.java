package org.example.product.service;

import org.example.product.model.Product;
import org.example.product.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private static final int PAGE_SIZE=10;

    @Autowired
    private ProductRepo productRepo;

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

    @Override
    public Product createProduct(Product product) {

        Product saveProduct = productRepo.save(product);

        productSearchEntryService.addProductToSearch(saveProduct);

        return saveProduct;
    }
}
