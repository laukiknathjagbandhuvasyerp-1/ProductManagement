package org.example.product.service;

import jakarta.persistence.PostPersist;
import org.example.product.model.ProductSearch;
import org.example.product.model.Variant;
import org.example.product.repo.ProductSearchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariantService {

    @Autowired
    private ProductSearchRepo productSearchRepo;

    @Autowired
    private VectorSearchService vectorSearchService;

    @PostPersist
    public void afterSave(Variant variant){

        String combineProductNameWithVariant = variant.getProduct().getProductName()+ " " + variant.getProductVariantName();

        String vectorStr = vectorSearchService.getVectorString(combineProductNameWithVariant);

        float[] embedding = convert(vectorStr);

        ProductSearch search = new ProductSearch();
        search.setProductId(variant.getProduct().getProductId());
        search.setVariantId(variant.getProductVariantId());
        search.setProductDetails(combineProductNameWithVariant);
        search.setSearchEmbedding(embedding);

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
}
