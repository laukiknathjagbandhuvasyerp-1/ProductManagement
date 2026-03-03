package org.example.product.service;

import org.example.product.dto.ProductSearchDTO;
import org.example.product.dto.SearchResultDTO;

import java.util.List;

public interface SearchService {

    public List<SearchResultDTO> searchAllProduct(String query,Long companyId);

}
