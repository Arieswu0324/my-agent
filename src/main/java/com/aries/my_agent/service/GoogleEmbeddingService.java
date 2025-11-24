package com.aries.my_agent.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoogleEmbeddingService {

    private final EmbeddingModel geminiDocumentEmbeddingModel;


    public GoogleEmbeddingService(EmbeddingModel geminiDocumentEmbeddingModel) {
        this.geminiDocumentEmbeddingModel = geminiDocumentEmbeddingModel;
    }

    //将列表中的每一个 String 对象分别转化为一个单独的 Vector。
    public EmbeddingResponse embedForResponse(List<String> texts) {
        return geminiDocumentEmbeddingModel.embedForResponse(texts);
    }

    public float[] embed(String text) {
        return geminiDocumentEmbeddingModel.embed(text);
    }





}