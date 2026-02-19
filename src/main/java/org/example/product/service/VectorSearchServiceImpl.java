package org.example.product.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VectorSearchServiceImpl implements VectorSearchService{

    @Autowired(required = true)
    private EmbeddingModel embeddingModel;

    @Override
    public String getVectorString(String query) {

        if (query == null || query.trim().isEmpty()) {
            return "[]";
        }

        float[] embeddingArray = embeddingModel.embed(query.trim());

        List<Double> embeddingList = new ArrayList<>();

        for (float f : embeddingArray) {
            embeddingList.add((double) f);
        }
        return embeddingList.toString();
    }
}
