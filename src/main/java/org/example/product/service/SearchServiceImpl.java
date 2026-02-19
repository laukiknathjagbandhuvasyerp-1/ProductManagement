package org.example.product.service;

import org.example.product.model.Product;
import org.example.product.repo.ProductRepo;
import org.example.product.repo.VariantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private VariantRepo variantRepo;

    @Autowired
    private VectorSearchService vectorSearchService;

    @Override
    public List<Object> searchAll(String query) {

        String vectorStr = vectorSearchService.getVectorString(query);

        List<Product> products = productRepo.findSimilarProducts(vectorStr,10);

        List<Object[]> variants = variantRepo.findSimilarVariants(vectorStr,10);

        List<Object> result = new ArrayList<>();
        result.addAll(products);
        result.addAll(variants);

        return result;
    }
}

