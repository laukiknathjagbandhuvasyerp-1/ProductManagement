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

    @PostMapping("/delete/{productId}")
    @ResponseBody
    public String deleteProduct (@PathVariable Long productId){
        productService.softDeleteProduct(productId);
        return "Product Deleted";
    }

    @PostMapping("/delete/variant/{variantId}")
    @ResponseBody
    public String deleteVariant (@PathVariable Long variantId){
        variantService.softDeleteVariant(variantId);
        return "Variant Deleted";
    }




}
