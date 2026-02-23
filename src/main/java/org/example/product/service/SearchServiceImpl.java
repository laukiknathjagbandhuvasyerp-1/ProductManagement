package org.example.product.service;

import org.example.product.dto.ProductSearchDTO;
import org.example.product.dto.SearchResultDTO;
import org.example.product.model.Product;
import org.example.product.model.ProductSearch;
import org.example.product.repo.ProductRepo;
import org.example.product.repo.ProductSearchRepo;
import org.example.product.repo.VariantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    private ProductSearchRepo productSearchRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private VariantRepo variantRepo;

    @Autowired
    private VectorSearchService vectorSearchService;

    @Override
    public List<Object> searchAll(String query) {

        String vectorStr =vectorSearchService.getVectorString(query) ;

        List<Map<String,String>> products = productRepo.findSimilarProducts(vectorStr,10);

        List<Map<String,String>> variants = variantRepo.findSimilarVariants(vectorStr,10);

        List<Object> result = new ArrayList<>();
        result.addAll(products);
        result.addAll(variants);

        return result;
    }

    @Override
    public List<SearchResultDTO> searchAllProduct(String query) {

        String vectorStr = vectorSearchService.getVectorString(query);

        List<String> results = productSearchRepo.findSimilar(vectorStr,10);

        return results.stream()
                .map(text-> SearchResultDTO.builder()
                        .displayText(text)
                        .build())
                        .toList();

    }

}

