package org.example.product.service;

import org.example.product.dto.ProductCreateDTO;
import org.example.product.dto.ProductResponseDTO;
import org.example.product.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

//    Page<Product> searchProducts(String product,int page);
//
//    List<Product> findSuggestions(String product,int limit);

    ProductResponseDTO createProduct(ProductCreateDTO product);
}
