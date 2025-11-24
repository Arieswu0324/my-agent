package com.aries.my_agent.configuration;

import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfig {

    @Value("${spring.ai.vectorstore.chroma.collection-name}")
    private String COLLECTION_NAME;

    @Value("${spring.ai.vectorstore.chroma.initialize-schema}")
    private boolean INITIALIZE_SCHEMA;

    @Bean
    public VectorStore vectorStore(ChromaApi chromaApi,
                                   @Qualifier("geminiQueryEmbeddingModel") EmbeddingModel queryModel) {
        // 这里注入的是“查询模型”
        return ChromaVectorStore.builder(chromaApi, queryModel)
                .collectionName(COLLECTION_NAME)
                .initializeSchema(INITIALIZE_SCHEMA).build();
    }
}
