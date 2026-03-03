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
    private VectorSearchService vectorSearchService;

    @Override
    public List<SearchResultDTO> searchAllProduct(String query,Long companyId) {

        String vectorStr = vectorSearchService.getVectorString(query);

        List<Object[]> results = productSearchRepo.findSimilarProduct(vectorStr,companyId,10);

        return results.stream()
                .map(row-> SearchResultDTO.builder()
                        .productId(((Number)row[0]).longValue())
                        .variantId(((Number)row[1]).longValue())
                        .displayText((String) row[2])
                        .build())
                .toList();

    }

}

