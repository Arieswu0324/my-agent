package com.aries.my_agent.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentChunkService {

    private final DocumentTransformer markdownHeaderSplitter;


    public DocumentChunkService(DocumentTransformer markdownHeaderSplitter) {
        this.markdownHeaderSplitter = markdownHeaderSplitter;
    }

    public List<Document> chunk(String filePath) {
        TikaDocumentReader reader = new TikaDocumentReader(filePath);
        List<Document> rawDocs = reader.get();

        return markdownHeaderSplitter.apply(rawDocs);
    }
}
