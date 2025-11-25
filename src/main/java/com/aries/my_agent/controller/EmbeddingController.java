package com.aries.my_agent.controller;


import com.aries.my_agent.service.DocumentChunkService;
import com.aries.my_agent.service.GoogleEmbeddingService;
import com.aries.my_agent.service.VectorIngestService;
import com.aries.my_agent.service.VectorSearchService;
import org.jsoup.internal.StringUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/embedding")
@CrossOrigin(origins = "*")
public class EmbeddingController {

    private final GoogleEmbeddingService googleEmbeddingService;

    private final VectorIngestService vectorIngestService;

    private final VectorSearchService vectorSearchService;

    private final DocumentChunkService documentChunkService;

    public record IngestTextRequest(String text) {
    }

    public record IngestFileRequest(String filePath) {
    }


    public EmbeddingController(GoogleEmbeddingService googleEmbeddingService, VectorIngestService vectorIngestService,
                               VectorSearchService vectorSearchService,
                               DocumentChunkService documentChunkService) {
        this.googleEmbeddingService = googleEmbeddingService;
        this.vectorIngestService = vectorIngestService;
        this.documentChunkService = documentChunkService;
        this.vectorSearchService = vectorSearchService;
    }

    @GetMapping("/embed_for_response")
    public Map embedForResponse(@RequestParam(value = "message", defaultValue = "Test embedding for response interface") String message) {
        EmbeddingResponse embeddingResponse = googleEmbeddingService.embedForResponse(List.of(message));
        System.out.println("USER INPUT: " + message);
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("embed")
    public Map embed(@RequestParam(value = "message", defaultValue = "Test embedding model") String message) {
        float[] vector = googleEmbeddingService.embed(message);
        return Map.of("vector", vector);
    }

    @PostMapping("/ingest")
    public Map ingestText(@RequestBody IngestTextRequest request) {

        Document document = new Document(request.text(), Map.of("autor", "Aries", "created_time", LocalDateTime.now()));
        vectorIngestService.ingest(List.of(document));
        return Map.of("Success", "message ingested");
    }

    @PostMapping("/ingest_file")
    public Map ingestFile(@RequestBody IngestFileRequest request) {
        List<Document> chunks = documentChunkService.chunk(request.filePath());
        vectorIngestService.ingest(chunks);
        return Map.of("Success", "file ingested");
    }

    @PostMapping("/chunk")
    public Map chunkFile(@RequestBody IngestFileRequest request) {
        List<Document> chunks = documentChunkService.chunk(request.filePath());
        return Map.of("Success", chunks);
    }

    @GetMapping("/search")
    public Map search(@RequestParam(value = "query") String query) {
        if (StringUtil.isBlank(query)) {
            return Map.of("400", "search query cannot be blank");
        }
        List<Document> result = vectorSearchService.search(query);
        List<String> list = result.stream().map(Document::getText).toList();
        return Map.of("contexts", list);

    }


}
