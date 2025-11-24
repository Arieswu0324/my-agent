package com.aries.my_agent.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VectorSearchService {

    private static final int TOP_K = 3;
    private final VectorStore vectorStore;

    public VectorSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> search(String query) {
        SearchRequest request = SearchRequest.builder().query(query).topK(TOP_K).build();
        return vectorStore.similaritySearch(request);
    }
}
