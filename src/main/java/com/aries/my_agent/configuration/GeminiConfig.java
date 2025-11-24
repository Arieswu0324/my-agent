package com.aries.my_agent.configuration;

import org.springframework.ai.google.genai.GoogleGenAiEmbeddingConnectionDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GeminiConfig {

    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;

    @Bean
    @Primary
    public GoogleGenAiEmbeddingConnectionDetails connectionDetails() {
        return GoogleGenAiEmbeddingConnectionDetails.builder()
                .apiKey(this.apiKey)
                .projectId("unused-id-bypass") // 关键：填个假值绕过 "project-id must be set"
                .location("us-central1")       // 关键：填个假值
                .build();
    }
}
