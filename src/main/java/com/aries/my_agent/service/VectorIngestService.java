package com.aries.my_agent.service;


import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class VectorIngestService {

    @Value("${spring.ai.vectorstore.chroma.collection-name}")
    private String COLLECTION_NAME;

    private static final String TENANT_NAME = "default_tenant";

    private static final String DATABASE_NAME = "default_database";

    private final EmbeddingModel geminiDocumentEmbeddingModel;

    private final ChromaApi chromaApi;

    public VectorIngestService(EmbeddingModel geminiQueryEmbeddingModel, ChromaApi chromaApi) {
        this.geminiDocumentEmbeddingModel = geminiQueryEmbeddingModel;
        this.chromaApi = chromaApi;
    }

    public void ingest(List<Document> documents) {

        ChromaApi.Collection collection = chromaApi.getCollection(TENANT_NAME, DATABASE_NAME, COLLECTION_NAME);

        if (collection == null) {
            throw new RuntimeException("Collection not found: " + COLLECTION_NAME);
        }

        String collectionId = collection.id();

        List<String> ids = new ArrayList<>();
        List<float[]> embeddings = new ArrayList<>();
        List<Map<String, Object>> metadatas = new ArrayList<>();
        List<String> contents = new ArrayList<>();

        for (Document document : documents) {
            ids.add(document.getId());
            metadatas.add(document.getMetadata());
            contents.add(document.getText());
            embeddings.add(geminiDocumentEmbeddingModel.embed(document));
        }

        ChromaApi.AddEmbeddingsRequest request = new ChromaApi.AddEmbeddingsRequest(ids, embeddings, metadatas, contents);

        chromaApi.upsertEmbeddings(TENANT_NAME, DATABASE_NAME, collectionId, request);
    }

}
