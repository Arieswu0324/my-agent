package com.aries.my_agent.configuration;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.google.genai.GoogleGenAiEmbeddingConnectionDetails;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingModel;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GeminiEmbeddingConfig {


    @Value("${spring.ai.google.genai.embedding.text.options.model}")
    private String modelName;

    @Bean
    @Primary
    EmbeddingModel geminiQueryEmbeddingModel(GoogleGenAiEmbeddingConnectionDetails connectionDetails) {
        GoogleGenAiTextEmbeddingOptions options = GoogleGenAiTextEmbeddingOptions.builder()
                .model(modelName)
                .taskType(GoogleGenAiTextEmbeddingOptions.TaskType.RETRIEVAL_QUERY)
                .build();

        return new GoogleGenAiTextEmbeddingModel(connectionDetails, options);
    }

    @Bean
    EmbeddingModel geminiDocumentEmbeddingModel(GoogleGenAiEmbeddingConnectionDetails connectionDetails) {

        GoogleGenAiTextEmbeddingOptions options = GoogleGenAiTextEmbeddingOptions.builder()
                .model(modelName)
                .taskType(GoogleGenAiTextEmbeddingOptions.TaskType.RETRIEVAL_DOCUMENT)
                .build();
        return new GoogleGenAiTextEmbeddingModel(connectionDetails, options);
    }


}
