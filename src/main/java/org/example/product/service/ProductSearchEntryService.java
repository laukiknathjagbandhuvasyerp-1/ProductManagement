package org.example.product.service;

import jakarta.persistence.PostPersist;
import org.example.product.model.Product;
import org.example.product.model.ProductSearch;
import org.example.product.model.ProductVariant;
import org.example.product.repo.ProductSearchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchEntryService {

    @Autowired
    private ProductSearchRepo productSearchRepo;

    @Autowired
    private VectorSearchService vectorSearchService;

//    @PostPersist
//    public void afterSave(ProductVariant productVariant){
//
//        String combineProductNameWithVariant = productVariant.getProduct().getProductName()+ " " + productVariant.getProductVariantName();
//
//        String vectorStr = vectorSearchService.getVectorString(combineProductNameWithVariant);
//
//        float[] embedding = convert(vectorStr);
//
//        ProductSearch search = new ProductSearch();
//        search.setProductId(productVariant.getProduct().getProductId());
//        search.setVariantId(productVariant.getProductVariantId());
//        search.setProductDetails(combineProductNameWithVariant);
//        search.setSearchEmbedding(embedding);
//
//        productSearchRepo.save(search);
//    }


    private void saveProductSearch(ProductVariant productVariant,String productDetail){

        String vectorStr =  vectorSearchService.getVectorString(productDetail);
        float[] embedding = convert(vectorStr);

        ProductSearch search = ProductSearch.builder()
                .productId(productVariant.getProduct().getProductId())
                .productVariant(productVariant)
                .productDetails(productDetail)
                .searchEmbedding(embedding)
                .companyId(0L)
                .build();

        productSearchRepo.save(search);
    }

    private float[] convert(String vectorStr){
        vectorStr=vectorStr.replace("[","").replace("]","");
        String [] parts = vectorStr.split(",");
        float[] arr= new float[parts.length];
        for(int i=0;i<parts.length;i++){
            arr[i]=Float.parseFloat(parts[i].trim());
        }
        return arr;
    }

//    public void addProductToSearch(Product product){
//        String productDetails = product.getProductName();
//        saveProductSearch(product,null,productDetails);
//    }

    public void addVariantsToSearch(ProductVariant productVariant){
        String productDetails = productVariant.getProduct().getProductName()+" "+productVariant.getProductVariantName();
        saveProductSearch(productVariant,productDetails);
    }

}
