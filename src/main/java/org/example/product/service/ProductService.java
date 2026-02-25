package org.example.product.service;

import org.example.product.dto.ProductCreateDTO;
import org.example.product.dto.ProductResponseDTO;
import org.example.product.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct(ProductCreateDTO product);

    void softDeleteProduct(Long productId);

    List<ProductResponseDTO> createProducts(List<ProductCreateDTO> productDTOs);

}
