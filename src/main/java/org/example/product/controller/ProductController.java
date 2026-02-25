package org.example.product.controller;

import org.example.product.dto.*;
import org.example.product.model.Product;
import org.example.product.service.ProductService;
import org.example.product.service.SearchService;
import org.example.product.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private VariantService variantService;

    @GetMapping("/list")
    public String showProductPage(){
        return "product-list";
    }

//    @GetMapping("/ajax/products/search")
//    @ResponseBody
//    public Page<Product> searchProduct(@RequestParam(required = false) String p,@RequestParam(defaultValue = "1") int page){
//        return productService.searchProducts(p,page);
//    }

//    @GetMapping("/ajax/products/suggest")
//    @ResponseBody
//    public List<Product> getSuggestions(@RequestParam String p){
//        return productService.findSuggestions(p,10);
//    }

//    @GetMapping("/ajax/vector-search")
//    @ResponseBody
//    public List<Object> vectorSearch(@RequestParam String q){
//        return searchService.searchAll(q);
//    }

    @GetMapping("/vector-search")
    public String showVector(){
        return "vector-search";
    }

    @GetMapping("/ajax/search")
    @ResponseBody
    public List<SearchResultDTO> search(@RequestParam String q){
        return searchService.searchAllProduct(q);
    }

    @PostMapping("/create")
    @ResponseBody
    public ProductResponseDTO createProduct(@RequestBody ProductCreateDTO dto){
        return productService.createProduct(dto);
    }

    @PostMapping("/variant/create")
    @ResponseBody
    public ProductVariantResponseDTO  createVariant(@RequestBody ProductVariantCreateDTO dto){
        return variantService.createProductVariant(dto);
    }



}
